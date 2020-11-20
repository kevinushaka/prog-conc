package td13pile;
public class Stat {
	private int added;
	private int notAdded;
	private int deleted;
	private int notDeleted;

	public void added() {
		added++;
	}

	public void notAdded() {
		notAdded++;
	}

	public void deleted() {
		deleted++;
	}

	public void notDeleted() {
		notDeleted++;
	}

	public void add(Stat stat) {
		added += stat.added;
		notAdded += stat.notAdded;
		deleted += stat.deleted;
		notDeleted += stat.notDeleted;
	}

	@Override
	public String toString() {
		return "Stat [" + "added=" + added + ", notAdded=" + notAdded + ", deleted=" + deleted + ", notDeleted=" + notDeleted +
			   ']';
	}
}
