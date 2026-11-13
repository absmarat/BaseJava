package com.github.absmarat.webapp.storage.junit4;

import com.github.absmarat.webapp.exception.ExistStorageException;
import com.github.absmarat.webapp.exception.NotExistStorageException;
import com.github.absmarat.webapp.exception.StorageException;
import org.junit.Assert;
import org.junit.Before;
import com.github.absmarat.webapp.model.Resume;
import com.github.absmarat.webapp.storage.Storage;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

import static com.github.absmarat.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);

    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
        assertEquals(0, storage.getAll().length);
    }

    @Test
    public void update() {
        Resume updated = new Resume(UUID_1);
        storage.update(updated);
        assertEquals(3, storage.size());
        assertSame(updated, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume resume_4 = new Resume(UUID_4);
        storage.update(resume_4);
    }

    @Test
    public void getAll() {
        Resume[] actuals = storage.getAll();
        Resume[] expecteds = {RESUME_1, RESUME_2, RESUME_3};
        Arrays.sort(actuals);
        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void save() {
        Resume expected = new Resume(UUID_4);
        storage.save(expected);
        assertEquals(4, storage.size());
        assertEquals(4, storage.getAll().length);
        assertEquals(expected, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_1);
    }

    @Test
    public void saveOverflow() {
        storage.clear();

        try {
            for (int i = 0; i < STORAGE_LIMIT; i++) {
                storage.save(new Resume("uuid" + i));
            }
        } catch (StorageException e) {
            fail("The overflow occurred earlier than expected!");
        }

        try {
            storage.save(new Resume("overflow_uuid"));
            fail("Expected StorageException was not thrown");
        } catch (StorageException e) {
            Assert.assertEquals("Storage overflow", e.getMessage());
        }
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        assertEquals(2, storage.getAll().length);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_4);
    }

    @Test
    public void get() {
        assertEquals(RESUME_1, storage.get(UUID_1));
        assertEquals(RESUME_2, storage.get(UUID_2));
        assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_4);
    }
}
