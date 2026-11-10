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

import static com.github.absmarat.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;


public abstract class AbstractArrayStorageTest {
    private Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);

    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
        Assert.assertEquals(0, storage.getAll().length);
    }

    @Test
    public void update() throws Exception {
        Resume updated = new Resume(UUID_1);
        storage.update(updated);
        Assert.assertEquals(3, storage.size());
        Assert.assertEquals(updated, storage.get(UUID_1));
    }

    @Test
    public void getAll() throws Exception {
        Resume[] actuals = storage.getAll();
        Resume[] expecteds = {RESUME_1, RESUME_2, RESUME_3};
        Arrays.sort(expecteds);
        Arrays.sort(actuals);
        Assert.assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void save() throws Exception {
        Resume resume_4 = new Resume("uuid_4");
        storage.save(resume_4);
        Assert.assertEquals(4, storage.size());
        Assert.assertEquals(4, storage.getAll().length);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test
    public void saveOverflow() throws Exception {
        storage.clear();

        try {
            for (int i = 0; i < STORAGE_LIMIT; i++) {
                storage.save(new Resume("uuid" + i));
            }
        } catch (StorageException e) {
            Assert.fail("The overflow occurred earlier than expected!");
        }

        try {
            storage.save(new Resume("overflow_uuid"));
            Assert.fail("Expected StorageException was not thrown");
        } catch (StorageException e) {
            Assert.assertEquals("Storage overflow", e.getMessage());
        }
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_1);
        Assert.assertEquals(2, storage.size());
        Assert.assertEquals(2, storage.getAll().length);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(RESUME_1, storage.get(UUID_1));
        Assert.assertEquals(RESUME_2, storage.get(UUID_2));
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}
