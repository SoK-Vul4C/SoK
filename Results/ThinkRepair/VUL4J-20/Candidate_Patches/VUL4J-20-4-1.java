public float[] toFloatArray()
{
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++)
    {
        Object obj = getObject(i);
        
        if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber) obj).floatValue();
        } else {
            // Handle the case if the object is not a COSNumber (e.g., throw an exception)
            throw new IllegalArgumentException("Object at index " + i + " is not a COSNumber");
        }
    }
    return retval;
}