public float[] toFloatArray() {
    float[] retval = new float[size()];
    
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case where the object is not a COSNumber
            throw new IllegalStateException("Object at index " + i + " is not a COSNumber");
        }
    }
    
    return retval;
}