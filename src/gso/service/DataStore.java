package gso.service;

import gso.model.AdminAccount;
import gso.model.AppData;
import gso.model.CounselorAccount;
import gso.model.SessionRecord;
import gso.model.StudentRecord;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

public class DataStore {
    private final File dataFile;

    public DataStore(String fileName) {
        this.dataFile = new File(fileName);
    }

    public AppData load() {
        if (!dataFile.exists()) {
            return new AppData();
        }

        try (ObjectInputStream input = new LegacyAwareObjectInputStream(new FileInputStream(dataFile))) {
            Object loaded = input.readObject();
            if (loaded instanceof AppData appData) {
                return appData;
            }
        } catch (Exception ignored) {
        }

        return new AppData();
    }

    public void save(AppData data) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            output.writeObject(data);
        }
    }

    private static final class LegacyAwareObjectInputStream extends ObjectInputStream {
        private LegacyAwareObjectInputStream(FileInputStream input) throws IOException {
            super(input);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass descriptor)
            throws IOException, ClassNotFoundException {
            return switch (descriptor.getName()) {
                case "GSOCounselingSystem$AppData" -> AppData.class;
                case "GSOCounselingSystem$AdminAccount" -> AdminAccount.class;
                case "GSOCounselingSystem$CounselorAccount" -> CounselorAccount.class;
                case "GSOCounselingSystem$StudentRecord" -> StudentRecord.class;
                case "GSOCounselingSystem$SessionRecord" -> SessionRecord.class;
                default -> super.resolveClass(descriptor);
            };
        }
    }
}
