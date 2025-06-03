public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // If the object is not a COSNumber, you can decide how to handle it
            // For now, let's set the value to 0.0f
            retval[i] = 0.0f;
        }
    }
    return retval;
}