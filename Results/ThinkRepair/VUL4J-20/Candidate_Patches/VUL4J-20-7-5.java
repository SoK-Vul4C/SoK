public float[] toFloatArray() {
    float[] retval = new float[size()];

    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);

        try {
            if (obj instanceof COSNumber) {
                retval[i] = ((COSNumber) obj).floatValue();
            } else {
                // Set a default value like 0.0f if the object is not a COSNumber
                retval[i] = 0.0f;
            }
        } catch (ClassCastException | NullPointerException e) {
            // Handle any exceptions that may occur during float value extraction
            retval[i] = 0.0f;
        }
    }

    return retval;
}