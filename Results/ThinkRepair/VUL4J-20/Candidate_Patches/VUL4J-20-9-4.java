public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        if (getObject(i) instanceof COSNumber) {
            COSNumber num = (COSNumber) getObject(i);
            retval[i] = num.floatValue();
        } else {
            throw new IllegalArgumentException("Object at index " + i + " is not a COSNumber");
        }
    }
    return retval;
}