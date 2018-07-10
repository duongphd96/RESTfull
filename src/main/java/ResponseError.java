public class ResponseError {
    private String message;

    public ResponseError( String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
