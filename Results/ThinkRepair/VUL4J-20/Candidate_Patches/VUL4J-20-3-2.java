public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the error by assigning a default value or skipping the element
            // For example, assigning 0.0 as the default value
            retval[i] = 0.0f;
            // Or you can skip the element by using continue
            // continue;
        }
    }
    return retval;
}