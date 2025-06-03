// Fixed Function
void read(Tokeniser t, CharacterReader r) {
    if (r.isEmpty()) {
        t.eofError(this);
        return;
    }

    char c = r.consume();
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
    }
}