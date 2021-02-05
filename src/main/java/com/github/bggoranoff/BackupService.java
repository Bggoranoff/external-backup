package com.github.bggoranoff;

import com.github.bggoranoff.model.files.Directory;
import com.github.bggoranoff.model.files.File;
import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.USBStorageDevice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BackupService {
    private final USBDeviceDetectorManager DEVICE_DETECTOR = new USBDeviceDetectorManager();
    private long lastTimeSynced = 0;

    public BackupService(long lastTimeSynced) {
        this.setLastTimeSynced(lastTimeSynced);
    }

    public void syncWithExternalDrive(String dirToSync, String externalDir) throws IOException {
        Directory local = new Directory(dirToSync);
        Directory external = new Directory(externalDir);
        if(!local.getContainedFile().exists()) {
            downloadExternalBackup(local.getContainedFile().getParent(), externalDir);
        } else if(!external.getContainedFile().exists()) {
            createExternalDriveBackup(dirToSync, external.getContainedFile().getParent());
        }
        for(java.io.File child : Objects.requireNonNull(local.getContainedFile().listFiles())) {
            if(child.isDirectory()) {
                this.syncWithExternalDrive(child.getPath(), externalDir + java.io.File.separator + child.getName());
            } else {
                File localChild = new File(child.getPath());
                File externalChild = new File(external.getContainedFile().getPath() + java.io.File.separator + child.getName());
                if(externalChild.getContainedFile().exists()) {
                    System.out.println("FILE EXISTS!!!" + externalChild.getContainedFile().getPath());
                    if(externalChild.getLastTimeModified() > localChild.getLastTimeModified()) {
                        localChild.delete();
                        externalChild.move(localChild.getContainedFile().getParent(), true);
                        new java.io.File(localChild.getContainedFile().getPath()).setLastModified(externalChild.getLastTimeModified());
                    } else if (externalChild.getLastTimeModified() < localChild.getLastTimeModified()) {
                        externalChild.delete();
                        localChild.move(externalChild.getContainedFile().getParent(), true);
                        new java.io.File(externalChild.getContainedFile().getPath()).setLastModified(localChild.getLastTimeModified());
                    }
                } else {
                    System.out.println("MOVING LOCAL FILE!!!");
                    localChild.move(externalChild.getContainedFile().getParent(), true);
                    new java.io.File(externalChild.getContainedFile().getPath()).setLastModified(localChild.getLastTimeModified());
                }
            }
        }
        syncExternalWithLocal(dirToSync, externalDir);
    }

    private void syncExternalWithLocal(String localDir, String externalDirToSync) throws IOException {
        Directory local = new Directory(localDir);
        Directory external = new Directory(externalDirToSync);
        if(!local.getContainedFile().exists()) {
            local.create();
        }
        for(java.io.File child : Objects.requireNonNull(external.getContainedFile().listFiles())) {
            if(child.isDirectory()) {
                this.syncExternalWithLocal(localDir + java.io.File.separator + child.getName(), child.getPath());
            } else {
                File externalChild = new File(child.getPath());
                File localChild = new File(local.getContainedFile().getPath() + java.io.File.separator + child.getName());
                if(!localChild.getContainedFile().exists()) {
                    if(this.lastTimeSynced <= externalChild.getLastTimeModified()) {
                        externalChild.move(localChild.getContainedFile().getParent(), true);
                        new java.io.File(localChild.getContainedFile().getPath()).setLastModified(externalChild.getLastTimeModified());
                    }
                }
            }
        }
    }

    public void createExternalDriveBackup(String dirToBackup, String externalDrive) throws IOException {
        Directory local = new Directory(dirToBackup);
        Directory external = new Directory(externalDrive + java.io.File.separator + local.getContainedFile().getName());
        if(!local.getContainedFile().exists()) {
            throw new FileNotFoundException(String.format("File %s not found!", local.getContainedFile().getPath()));
        } else if (!new java.io.File(externalDrive).exists()) {
            throw new FileNotFoundException(String.format("File %s not found!", externalDrive));
        }
        external.create();
        for(java.io.File child : Objects.requireNonNull(local.getContainedFile().listFiles())) {
            if(child.isDirectory()) {
                createExternalDriveBackup(child.getPath(), external.getContainedFile().getPath());
            } else {
                File localChild = new File(child.getPath());
                localChild.move(external.getContainedFile().getPath(), true);
                new java.io.File(external.getContainedFile().getPath()).setLastModified(localChild.getLastTimeModified());
            }
        }
    }

    public void downloadExternalBackup(String dest, String externalDir) throws IOException {
        Directory external = new Directory(externalDir);
        Directory local = new Directory(dest + java.io.File.separator + external.getContainedFile().getName());
        if(!external.getContainedFile().exists()) {
            throw new FileNotFoundException(String.format("File %s not found!", external.getContainedFile().getPath()));
        } else if (!new java.io.File(dest).exists()) {
            throw new FileNotFoundException(String.format("File %s not found!", dest));
        }
        local.create();
        for(java.io.File child : Objects.requireNonNull(external.getContainedFile().listFiles())) {
            if(child.isDirectory()) {
                createExternalDriveBackup(local.getContainedFile().getPath(), child.getPath());
            } else {
                File externalChild = new File(child.getPath());
                externalChild.move(local.getContainedFile().getPath(), true);
                new java.io.File(local.getContainedFile().getPath()).setLastModified(externalChild.getLastTimeModified());
            }
        }
    }



    public List<java.io.File> listRemovableDevices() {
        return this.DEVICE_DETECTOR.getRemovableDevices().stream().map(USBStorageDevice::getRootDirectory).collect(Collectors.toList());
    }

    public void setLastTimeSynced(long lastTimeSynced) {
        this.lastTimeSynced = lastTimeSynced;
    }
}
