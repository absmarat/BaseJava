package com.github.absmarat.webapp.storage;

import com.github.absmarat.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size;

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
            return;
        }
        storage[size++] = resume;
    }

    private void update(Resume resume, int index) {
        if (resume == null) return;

        String uuid = resume.getUuid();
        if (uuid == null || uuid.isBlank()) return;
        storage[index] = resume;
        System.out.println("Резюме " + resume + " обновлено!");
    }

    public Resume get(String uuid) {
        int index = findIndexByUuid(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("\nРезюме " + uuid + " не найдено!");
        return null;
    }

    public void delete(String uuid) {
        int index = findIndexByUuid(uuid);
        if (index == -1) {
            System.out.println("\nРезюме " + uuid + " не найдено!");
            return;
        }

        if (index >= 0 && index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        }
        storage[--size] = null;
        System.out.println("\nРезюме " + uuid + " удалено!");
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
