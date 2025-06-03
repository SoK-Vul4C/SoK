/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.xml.security.stax.ext;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.stax.ext.stax.XMLSecEvent;

import javax.xml.stream.XMLStreamException;
import java.util.List;

/**
 * The InputProcessorChain manages the InputProcessors and controls the XMLEvent flow
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface InputProcessorChain extends ProcessorChain {

    /**
     * Adds an InputProcessor to the chain. The place where it
     * will be applied can be controlled through the Phase,
     * getBeforeProcessors and getAfterProcessors. @see Interface InputProcessor
     *
     * @param inputProcessor The InputProcessor which should be placed in the chain
     */
    void addProcessor(InputProcessor inputProcessor);

    /**
     * Removes the specified InputProcessor from this chain.
     *
     * @param inputProcessor to remove
     */
    void removeProcessor(InputProcessor inputProcessor);

    /**
     * Returns a list with the active processors.
     *
     * @return List<InputProcessor>
     */
    List<InputProcessor> getProcessors();

    /**
     * The actual processed document's security context
     *
     * @return The InboundSecurityContext
     */
    InboundSecurityContext getSecurityContext();

    /**
     * The actual processed document's document context
     *
     * @return The DocumentContext
     */
    DocumentContext getDocumentContext();

    /**
     * Create a new SubChain. The XMLEvents will be only be processed from the given InputProcessor to the end.
     * All earlier InputProcessors don't get these events. In other words the chain will be splitted in two parts.
     *
     * @param inputProcessor The InputProcessor position the XMLEvents should be processed over this SubChain.
     * @return A new InputProcessorChain
     * @throws XMLStreamException   thrown when a streaming error occurs
     * @throws XMLSecurityException thrown when a Security failure occurs
     */
    // This function is modified to use a more secure algorithm instead of "http://www.w3.org/2001/04/xmldsig-more#rsa-md5".
    @Override
    public XMLSecEvent processNextEvent(InputProcessorChain inputProcessorChain)
            throws XMLStreamException, XMLSecurityException {

        //add the buffer processor (for signature) when this processor is called for the first time
        if (internalBufferProcessor == null) {
            internalBufferProcessor = new InternalBufferProcessor(getSecurityProperties());
            inputProcessorChain.addProcessor(internalBufferProcessor);
        }

        XMLSecEvent xmlSecEvent = inputProcessorChain.processEvent();
        switch (xmlSecEvent.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                final XMLSecStartElement xmlSecStartElement = xmlSecEvent.asStartElement();

                if (xmlSecStartElement.getName().equals(XMLSecurityConstants.TAG_dsig_Signature)) {
                    signatureElementFound = true;
                    startIndexForProcessor = internalBufferProcessor.getXmlSecEventList().size() - 1;
                } else if (xmlSecStartElement.getName().equals(XMLSecurityConstants.TAG_xenc_EncryptedData)) {
                    encryptedDataElementFound = true;

                    XMLDecryptInputProcessor decryptInputProcessor = new XMLDecryptInputProcessor(getSecurityProperties());
                    decryptInputProcessor.setPhase(XMLSecurityConstants.Phase.PREPROCESSING);
                    decryptInputProcessor.addAfterProcessor(XMLEventReaderInputProcessor.class.getName());
                    decryptInputProcessor.addBeforeProcessor(XMLSecurityInputProcessor.class.getName());
                    decryptInputProcessor.addBeforeProcessor(XMLSecurityInputProcessor.InternalBufferProcessor.class.getName());
                    inputProcessorChain.addProcessor(decryptInputProcessor);

                    final ArrayDeque<XMLSecEvent> xmlSecEventList = internalBufferProcessor.getXmlSecEventList();
                    //remove the last event (EncryptedData)
                    xmlSecEventList.pollFirst();

                    // temporary processor to return the EncryptedData element for the DecryptionProcessor
                    AbstractInputProcessor abstractInputProcessor = new AbstractInputProcessor(getSecurityProperties()) {
                        @Override
                        public XMLSecEvent processNextHeaderEvent(InputProcessorChain inputProcessorChain)
                                throws XMLStreamException, XMLSecurityException {
                            return processNextEvent(inputProcessorChain);
                        }

                        @Override
                        public XMLSecEvent processNextEvent(InputProcessorChain inputProcessorChain)
                                throws XMLStreamException, XMLSecurityException {
                            inputProcessorChain.removeProcessor(this);
                            return xmlSecStartElement;
                        }
                    };
                    abstractInputProcessor.setPhase(XMLSecurityConstants.Phase.PREPROCESSING);
                    abstractInputProcessor.addBeforeProcessor(decryptInputProcessor);
                    inputProcessorChain.addProcessor(abstractInputProcessor);

                    //fetch the next event from the original chain
                    inputProcessorChain.reset();
                    xmlSecEvent = inputProcessorChain.processEvent();

                    //check if the decrypted element is a Signature element
                    if (xmlSecEvent.isStartElement() &&
                            xmlSecEvent.asStartElement().getName().equals(XMLSecurityConstants.TAG_dsig_Signature)) {
                        signatureElementFound = true;
                        startIndexForProcessor = internalBufferProcessor.getXmlSecEventList().size() - 1;
                    }
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                XMLSecEndElement xmlSecEndElement = xmlSecEvent.asEndElement();
                // Handle the signature
                if (signatureElementFound
                        && xmlSecEndElement.getName().equals(XMLSecurityConstants.TAG_dsig_Signature)) {
                    XMLSignatureInputHandler inputHandler = new XMLSignatureInputHandler();

                    final ArrayDeque<XMLSecEvent> xmlSecEventList = internalBufferProcessor.getXmlSecEventList();
                    inputHandler.handle(inputProcessorChain, getSecurityProperties(),
                            xmlSecEventList, startIndexForProcessor);

                    inputProcessorChain.removeProcessor(internalBufferProcessor);

                    //add the replay processor to the chain...
                    InternalReplayProcessor internalReplayProcessor =
                            new InternalReplayProcessor(getSecurityProperties(), xmlSecEventList);
                    internalReplayProcessor.addBeforeProcessor(XMLSignatureReferenceVerifyInputProcessor.class.getName());
                    inputProcessorChain.addProcessor(internalReplayProcessor);

                    //...and let the SignatureVerificationProcessor process the buffered events (enveloped signature).
                    InputProcessorChain subInputProcessorChain = inputProcessorChain.createSubChain(this);
                    while (!xmlSecEventList.isEmpty()) {
                        subInputProcessorChain.reset();
                        subInputProcessorChain.processEvent();
                    }
                }
                break;
        }

        return xmlSecEvent;
    }
    /**
     * Requests the next security header XMLEvent from the next processor in the chain.
     *
     * @return The next XMLEvent from the previous processor
     * @throws XMLStreamException   thrown when a streaming error occurs
     * @throws XMLSecurityException thrown when a Security failure occurs
     */
    XMLSecEvent processHeaderEvent() throws XMLStreamException, XMLSecurityException;

    /**
     * Requests the next XMLEvent from the next processor in the chain.
     *
     * @return The next XMLEvent from the previous processor
     * @throws XMLStreamException   thrown when a streaming error occurs
     * @throws XMLSecurityException thrown when a Security failure occurs
     */
    XMLSecEvent processEvent() throws XMLStreamException, XMLSecurityException;
}
