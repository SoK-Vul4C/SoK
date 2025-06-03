private void writeSession(SessionInformations session, boolean displayUser) throws IOException {
    final String nextColumnAlignRight = "</td><td align='right'>";
    final String nextColumnAlignCenter = "</td><td align='center'>";
    write("<td><a href='?part=sessions&amp;sessionId=");
    write(htmlEncodeButNotSpace(session.getId()));
    write("'>");
    write(htmlEncodeButNotSpace(session.getId()));
    write("</a>");
    write(nextColumnAlignRight);
    write(durationFormat.format(session.getLastAccess()));
    write(nextColumnAlignRight);
    write(durationFormat.format(session.getAge()));
    write(nextColumnAlignRight);
    write(expiryFormat.format(session.getExpirationDate()));

    write(nextColumnAlignRight);
    write(integerFormat.format(session.getAttributeCount()));
    write(nextColumnAlignCenter);
    if (session.isSerializable()) {
        write("#oui#");
    } else {
        write("<span class='severe'>#non#</span>");
    }
    write(nextColumnAlignRight);
    write(integerFormat.format(session.getSerializedSize()));
    final String nextColumn = "</td><td>";
    write(nextColumn);
    if (session.getRemoteAddr() == null) {
        write("&nbsp;");
    } else {
        write(htmlEncodeButNotSpace(session.getRemoteAddr())); // Fix: HTML encode remoteAddr
    }
    write(nextColumnAlignCenter);
    writeCountry(session);
    if (displayUser) {
        write(nextColumn);
        if (session.getRemoteUser() == null) {
            write("&nbsp;");
        } else {
            writeDirectly(htmlEncodeButNotSpace(session.getRemoteUser()));
        }
    }
    write("</td><td align='center' class='noPrint'>");
    write(A_HREF_PART_SESSIONS);
    write("&amp;action=invalidate_session&amp;sessionId=");
    write(urlEncode(session.getId()));
    write("' onclick=\"javascript:return confirm('"
            + getStringForJavascript("confirm_invalidate_session") + "');\">");
    write("<img width='16' height='16' src='?resource=user-trash.png' alt='#invalidate_session#' title='#invalidate_session#' />");
    write("</a>");
    write("</td>");
}