public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Skip this entry and assign a default value
            retval[i] = 0.0f; // Or you can handle it differently based on your requirements
        }
    }
    return retval;
}