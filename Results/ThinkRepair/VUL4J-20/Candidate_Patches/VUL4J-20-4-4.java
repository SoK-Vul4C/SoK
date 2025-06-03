public float[] toFloatArray() {
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case when the object is not a COSNumber
            // Here, we skip the entry by assigning NaN (Not-a-Number)
            retval[i] = Float.NaN;
            // Alternatively, you can throw an exception here
            // throw new IllegalArgumentException("Object at index " + i + " is not a COSNumber");
        }
    }
    return retval;
}