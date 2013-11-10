package tools;

public enum Directions {
	UP("IP"), DOWN("DOWN");
	
    /**
     * @param text
     */
    private Directions(final String text) {
        this.text = text;
    }

    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
