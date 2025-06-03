public float[] toFloatArray() {
    float[] retval = new float[size()];

    for (int i = 0; i < size(); i++) {
        Object obj = getObject(i);
        if (obj instanceof COSNumber) {
            COSNumber cosNumber = (COSNumber) obj;
            retval[i] = cosNumber.floatValue();
        } else {
            System.err.println("Object at index " + i + " is not a COSNumber. Skipping.");
            // Skip this index or assign a default value
            continue;
        }
    }

    return retval;
}