package manager.presenter;

public interface MapChangeListener {
    void mapChanged();

    void setErrorMessage(String errorMessage, int index);
}
