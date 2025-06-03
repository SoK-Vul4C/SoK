public float[] toFloatArray()
{
    float[] retval = new float[size()];
    for (int i = 0; i < size(); i++)
    {
        Object obj = getObject(i);
        if (obj == null) {
            retval[i] = 0.0f; // Set default value if object is null
        } else if (obj instanceof COSNumber) {
            retval[i] = ((COSNumber)obj).floatValue(); // Cast to COSNumber and get floatValue
        } else {
            retval[i] = 0.0f; // Set default value if object is not a COSNumber
        }
    }
    return retval;
}