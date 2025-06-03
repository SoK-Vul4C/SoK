public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the error by either setting a default value or skipping the element
            // In this case, we assign 0.0 as the default value for non-COSNumber objects
            retval[i] = 0.0f;
        }
    }
    return retval;
}