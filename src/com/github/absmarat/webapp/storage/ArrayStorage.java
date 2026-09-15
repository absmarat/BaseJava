package com.github.absmarat.webapp.storage;

import com.github.absmarat.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private int size;
    private Resume[] storage = new Resume[10000];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume resume) {
        if (resume == null) return;

        String uuid = resume.getUuid();
        if (uuid == null || uuid.isBlank()) return;
        if (size >= storage.length) {
            System.out.println("Хранилище резюме переполнено!");
            return;
        }

        int index = findIndexByUuid(uuid);
        if (index != -1) {
            System.out.println("Резюме " + uuid + " уже существует");
            update(resume, index);
            return;
        }
        storage[size++] = resume;
    }

    private void update(Resume resume) {
        if (resume == null) return;
        if (resume.getUuid() == null || resume.getUuid().isBlank()) return;

        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(resume.getUuid())) {
                storage[i] = resume;
                System.out.println("Резюме " + resume + " обнавлёно!");
                return;
            }
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                if (i < size - 1) {
                    System.arraycopy(storage, i + 1, storage, i, size - i - 1);
                }
                storage[--size] = null;
                System.out.println("\nРезюме " + uuid + " удалено!");
                return;
            }
        }
        System.out.println("Резюме " + uuid + " не найдено!");
    }

    private int findIndexByUuid(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
