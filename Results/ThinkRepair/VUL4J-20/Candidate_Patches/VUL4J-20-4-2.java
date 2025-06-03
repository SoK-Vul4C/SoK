public float[] toFloatArray()
{
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++)
    {
        Object obj = getObject(i);

        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case when the object is not a COSNumber
            // For example, skip the entry by assigning 0 or throw an exception
            // Here, we assign 0 if the object is not a COSNumber
            retval[i] = 0.0f;
            // Alternatively, you can throw an exception here
            // throw new IllegalArgumentException("Object at index " + i + " is not a COSNumber");
        }
    }
    return retval;
}