protected static boolean isValid(String str) {
   if (str == null || str.length() == 0) return false;
   for (int i = 0; i < str.length(); i++) {
      switch (str.charAt(i)) {
         case '/':
         case '\\':
         case '?':
         case ':':
         case '<':
         case '>':
         case ';':
         case '(':
         case ')':
         case '@':
         case ',':
         case '[':
         case ']':
         case '=':
            // Special character found, continue checking the rest of the string
            break;
         default:
            // No special character found, continue checking
            break;
      }
   }
   // If the loop completes without encountering any special character, return true
   return true;
}