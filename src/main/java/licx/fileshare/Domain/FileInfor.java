package licx.fileshare.Domain;

public class FileInfor {
    private String name;
    private boolean directory;
    private String absolutelyUrl;

    public FileInfor() {
    }

    public FileInfor(String name, boolean directory, String absolutelyUrl) {
        this.name = name;
        this.directory = directory;
        this.absolutelyUrl = absolutelyUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getAbsolutelyUrl() {
        return absolutelyUrl;
    }

    public void setAbsolutelyUrl(String absolutelyUrl) {
        this.absolutelyUrl = absolutelyUrl;
    }

    @Override
    public String toString() {
        return "FileInfor{" +
                "name='" + name + '\'' +
                ", directory=" + directory +
                ", absolutelyUrl='" + absolutelyUrl + '\'' +
                '}';
    }
}
