public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case where the object is not a COSNumber
            // Set a default value (e.g., 0.0f) for non-COSNumber objects
            // Adjust this handling based on your requirements
            retval[i] = 0.0f;
        }
    }
    return retval;
}