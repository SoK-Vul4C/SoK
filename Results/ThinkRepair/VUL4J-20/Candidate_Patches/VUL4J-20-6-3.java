public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            COSNumber cosNumber = (COSNumber) obj;
            retval[i] = cosNumber.floatValue();
        } else {
            throw new IllegalArgumentException("Object at index " + i + " is not a COSNumber");
        }
    }
    return retval;
}