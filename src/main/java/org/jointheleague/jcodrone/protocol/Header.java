package org.jointheleague.jcodrone.protocol;

public class Header implements Serializable {
    private static final int SIZE = 2;
    private DataType dataType = DataType.NONE;
    private int length = 0;


    @Override
    public byte[] toArray() {
        // TODO pack('<BB', self.dataType.value, self.length)
        return null;
    }

    static Header parse(byte[] dataArray) {
        Header header = new Header();
        if (dataArray.length != SIZE) {
            return null;
        }

        //TODO fix
        //header.dataType, header.length = unpack('<BB', dataArray)

        header.dataType = new DataType(header.dataType);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setLength(byte[] length) {
        this.length = length[0];
    }

    public int getLength() {
        return length;
    }

    public DataType getDataType() {
        return dataType;
    }
}

