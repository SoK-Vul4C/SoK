void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    boolean insideTag = false; // Flag to track if we are inside a tag

    switch (c) {
        case '\t':
        case '\n':
        case '\r':
        case '\f':
        case ' ':
            // ignore
            break;
        case '"':
            t.transition(AttributeValue_doubleQuoted);
            break;
        case '&':
            r.unconsume();
            t.transition(AttributeValue_unquoted);
            break;
        case '\'':
            t.transition(AttributeValue_singleQuoted);
            break;
        case nullChar:
            t.error(this);
            t.tagPending.appendAttributeValue(replacementChar);
            t.transition(AttributeValue_unquoted);
            break;
        case eof:
            if (t.getCurrentToken().type == TokenType.StartTag || t.getCurrentToken().type == TokenType.EndTag) {
                t.error(this);
                t.emitTagPending();
                t.transition(Data);
            } else {
                t.eofError(this);
            }
            break;
        case '>':
            t.error(this);
            t.emitTagPending();
            t.transition(Data);
            break;
        case '<':
        case '=':
        case '`':
            t.error(this);
            t.tagPending.appendAttributeValue(c);
            t.transition(AttributeValue_unquoted);
            break;
        default:
            r.unconsume();
            t.transition(AttributeValue_unquoted);
            insideTag = true; // We are inside a tag
    }

    // If we are inside a tag and encountering EOF, handle it accordingly
    if (insideTag && c == eof) {
        t.error(this);
        t.emitTagPending();
        t.transition(Data);
    }
}