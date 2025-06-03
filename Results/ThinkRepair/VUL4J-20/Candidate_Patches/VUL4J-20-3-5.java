public float[] toFloatArray() {
    float[] retval = new float[size()];
    int index = 0;
    
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[index++] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the error by skipping the element
            // You can also choose to assign a default value like 0.0f
            // retval[index++] = 0.0f;
        }
    }

    // Copy the actual elements to a new array of correct size
    float[] result = Arrays.copyOf(retval, index);

    return result;
}