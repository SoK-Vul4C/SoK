// Fixed Function
void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    switch (c) {
        case '\t':
        case '\n':
        case '\r':
        case '\f':
        case ' ':
            // Ignore whitespace characters
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
        case '\u0000': // Compare against nullChar character constant
            t.error(this);
            t.tagPending.appendAttributeValue(replacementChar);
            t.transition(AttributeValue_unquoted);
            break;
        case eof:
            t.eofError(this);
            t.transition(Data);
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
            // Unconsume and transition to unquoted attribute value state
            r.unconsume();
            t.transition(AttributeValue_unquoted);
    }
}