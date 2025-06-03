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
package org.apache.xml.security.stax.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.stax.ext.*;
import org.apache.xml.security.stax.ext.stax.XMLSecEvent;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a InputProcessorChain
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class InputProcessorChainImpl implements InputProcessorChain {

    protected static final transient Logger log = LoggerFactory.getLogger(InputProcessorChainImpl.class);
    protected static final transient boolean isDebugEnabled = log.isDebugEnabled();

    private List<InputProcessor> inputProcessors;
    private int startPos = 0;
    private int curPos = 0;

    private final InboundSecurityContext inboundSecurityContext;
    private final DocumentContextImpl documentContext;

    public InputProcessorChainImpl(InboundSecurityContext inboundSecurityContext) {
        this(inboundSecurityContext, 0);
    }

    public InputProcessorChainImpl(InboundSecurityContext inboundSecurityContext, int startPos) {
        this(inboundSecurityContext, new DocumentContextImpl(), startPos, new ArrayList<InputProcessor>(20));
    }

    public InputProcessorChainImpl(InboundSecurityContext inboundSecurityContext, DocumentContextImpl documentContext) {
        this(inboundSecurityContext, documentContext, 0, new ArrayList<InputProcessor>(20));
    }

    protected InputProcessorChainImpl(InboundSecurityContext inboundSecurityContext, DocumentContextImpl documentContextImpl,
                                      int startPos, List<InputProcessor> inputProcessors) {
        this.inboundSecurityContext = inboundSecurityContext;
        this.curPos = this.startPos = startPos;
        this.documentContext = documentContextImpl;
        this.inputProcessors = inputProcessors;
    }

    @Override
    public void reset() {
        this.curPos = startPos;
    }

    @Override
    public InboundSecurityContext getSecurityContext() {
        return this.inboundSecurityContext;
    }

    @Override
    public DocumentContext getDocumentContext() {
        return this.documentContext;
    }

    @Override
    public synchronized void addProcessor(InputProcessor newInputProcessor) {
        int startPhaseIdx = 0;
        int endPhaseIdx = inputProcessors.size();

        XMLSecurityConstants.Phase targetPhase = newInputProcessor.getPhase();

        for (int i = inputProcessors.size() - 1; i >= 0; i--) {
            InputProcessor inputProcessor = inputProcessors.get(i);
            if (inputProcessor.getPhase().ordinal() > targetPhase.ordinal()) {
                startPhaseIdx = i + 1;
                break;
            }
        }
        for (int i = startPhaseIdx; i < inputProcessors.size(); i++) {
            InputProcessor inputProcessor = inputProcessors.get(i);
            if (inputProcessor.getPhase().ordinal() < targetPhase.ordinal()) {
                endPhaseIdx = i;
                break;
            }
        }

        //just look for the correct phase and append as last
        if (newInputProcessor.getBeforeProcessors().isEmpty()
                && newInputProcessor.getAfterProcessors().isEmpty()) {
            inputProcessors.add(startPhaseIdx, newInputProcessor);
        } else if (newInputProcessor.getBeforeProcessors().isEmpty()) {
            int idxToInsert = startPhaseIdx;

            for (int i = endPhaseIdx - 1; i >= startPhaseIdx; i--) {
                InputProcessor inputProcessor = inputProcessors.get(i);
                if (newInputProcessor.getAfterProcessors().contains(inputProcessor)
                        || newInputProcessor.getAfterProcessors().contains(inputProcessor.getClass().getName())) {
                    idxToInsert = i;
                    break;
                }
            }
            inputProcessors.add(idxToInsert, newInputProcessor);
        } else if (newInputProcessor.getAfterProcessors().isEmpty()) {
            int idxToInsert = endPhaseIdx;

            for (int i = startPhaseIdx; i < endPhaseIdx; i++) {
                InputProcessor inputProcessor = inputProcessors.get(i);
                if (newInputProcessor.getBeforeProcessors().contains(inputProcessor)
                        || newInputProcessor.getBeforeProcessors().contains(inputProcessor.getClass().getName())) {
                    idxToInsert = i + 1;
                    break;
                }
            }
            inputProcessors.add(idxToInsert, newInputProcessor);
        } else {
            boolean found = false;
            int idxToInsert = startPhaseIdx;

            for (int i = endPhaseIdx - 1; i >= startPhaseIdx; i--) {
                InputProcessor inputProcessor = inputProcessors.get(i);
                if (newInputProcessor.getAfterProcessors().contains(inputProcessor)
                        || newInputProcessor.getAfterProcessors().contains(inputProcessor.getClass().getName())) {
                    idxToInsert = i;
                    found = true;
                    break;
                }
            }
            if (found) {
                inputProcessors.add(idxToInsert, newInputProcessor);
            } else {
                for (int i = startPhaseIdx; i < endPhaseIdx; i++) {
                    InputProcessor inputProcessor = inputProcessors.get(i);
                    if (newInputProcessor.getBeforeProcessors().contains(inputProcessor)
                            || newInputProcessor.getBeforeProcessors().contains(inputProcessor.getClass().getName())) {
                        idxToInsert = i + 1;
                        break;
                    }
                }
                inputProcessors.add(idxToInsert, newInputProcessor);
            }
        }
        if (isDebugEnabled) {
            log.debug("Added " + newInputProcessor.getClass().getName() + " to input chain: ");
            for (int i = 0; i < inputProcessors.size(); i++) {
                InputProcessor inputProcessor = inputProcessors.get(i);
                log.debug("Name: " + inputProcessor.getClass().getName() + " phase: " + inputProcessor.getPhase());
            }
        }
    }

    @Override
    public synchronized void removeProcessor(InputProcessor inputProcessor) {
        if (isDebugEnabled) {
            log.debug("Removing processor " + inputProcessor.getClass().getName() + " from input chain");
        }
        if (this.inputProcessors.indexOf(inputProcessor) <= curPos) {
            this.curPos--;
        }
        this.inputProcessors.remove(inputProcessor);
    }

    @Override
    public List<InputProcessor> getProcessors() {
        return this.inputProcessors;
    }

    @Override
    public XMLSecEvent processHeaderEvent() throws XMLStreamException, XMLSecurityException {
        return inputProcessors.get(this.curPos++).processNextHeaderEvent(this);
    }

    @Override
    public XMLSecEvent processEvent() throws XMLStreamException, XMLSecurityException {
        return inputProcessors.get(this.curPos++).processNextEvent(this);
    }

    @Override
    public void doFinal() throws XMLStreamException, XMLSecurityException {
        inputProcessors.get(this.curPos++).doFinal(this);
    }

Function ID: 2
Fixed function code:
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
    }}
