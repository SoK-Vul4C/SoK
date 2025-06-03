public float[] toFloatArray()
{
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++)
    {
        Object obj = getObject(i);
        if (obj != null && obj instanceof COSNumber) {
            retval[i] = ((COSNumber)obj).floatValue(); // Cast to COSNumber and get floatValue
        } else {
            // Handle the case where the object is null or not a COSNumber
            // For example, you can set a default value or throw an exception
            retval[i] = 0.0f; // Setting a default value of 0.0
        }
    }
    return retval;
}