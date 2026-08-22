import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size;
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume resume) {
        if (resume == null) return;
        if (resume.uuid == null || resume.uuid.isBlank()) return;
        if (size >= storage.length) return;

        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(resume.uuid)) {
                storage[i] = resume;
                return;
            }
        }
        storage[size++] = resume;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid != null && storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                if (i < size - 1) {
                    System.arraycopy(storage, i + 1, storage, i, size - i - 1);
                }
                storage[--size] = null;
                return;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
