public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // If the object is not a COSNumber, handle it appropriately
            // For now, let's set it to NaN (Not a Number)
            retval[i] = Float.NaN;
        }
    }
    return retval;
}