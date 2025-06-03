public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case where the object is not a COSNumber
            // For now, let's set it to 0.0f
            retval[i] = 0.0f;
        }
    }
    return retval;
}