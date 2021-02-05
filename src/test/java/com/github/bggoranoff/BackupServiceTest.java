package com.github.bggoranoff;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class BackupServiceTest {
    @Test
    public void listExternalDriveShouldReturnFileList() {
        BackupService mockService = mock(BackupService.class);
        doReturn(new ArrayList<java.io.File>()).when(mockService).listRemovableDevices();
        assertEquals(new ArrayList<java.io.File>(), mockService.listRemovableDevices());
    }

    @Test
    public void syncWithExternalDriveShouldHandleValidInput() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doNothing().when(mockService).syncWithExternalDrive("/valid/dir/to/sync/", "/valid/external/dir/");
        mockService.syncWithExternalDrive("/valid/dir/to/sync/", "/valid/external/dir/");
        verify(mockService, times(1)).syncWithExternalDrive("/valid/dir/to/sync/", "/valid/external/dir/");
    }

    @Test(expected = FileNotFoundException.class)
    public void syncWithExternalDriveShouldThrowFileNotFoundException() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doThrow(new FileNotFoundException("Resource not found!")).when(mockService).syncWithExternalDrive("/invalid/dir/to/sync/", "/invalid/external/dir/");
        mockService.syncWithExternalDrive("/invalid/dir/to/sync/", "/invalid/external/dir/");
    }

    @Test
    public void createExternalDriveBackupShouldHandleValidInput() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doNothing().when(mockService).createExternalDriveBackup("/valid/dir/to/sync/", "/valid/external/dir/");
        mockService.createExternalDriveBackup("/valid/dir/to/sync/", "/valid/external/dir/");
        verify(mockService, times(1)).createExternalDriveBackup("/valid/dir/to/sync/", "/valid/external/dir/");
    }

    @Test(expected = FileNotFoundException.class)
    public void createExternalDriveBackupShouldThrowFileNotFoundException() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doThrow(new FileNotFoundException("Resource not found!")).when(mockService).createExternalDriveBackup("/invalid/dir/to/sync/", "/invalid/external/dir/");
        mockService.createExternalDriveBackup("/invalid/dir/to/sync/", "/invalid/external/dir/");
    }

    @Test
    public void downloadExternalBackupShouldHandleValidInput() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doNothing().when(mockService).downloadExternalBackup("/valid/dir/to/sync/", "/valid/external/dir/");
        mockService.downloadExternalBackup("/valid/dir/to/sync/", "/valid/external/dir/");
        verify(mockService, times(1)).downloadExternalBackup("/valid/dir/to/sync/", "/valid/external/dir/");
    }

    @Test(expected = FileNotFoundException.class)
    public void downloadExternalBackupShouldThrowFileNotFoundException() throws IOException {
        BackupService mockService = mock(BackupService.class);
        doThrow(new FileNotFoundException("Resource not found!")).when(mockService).downloadExternalBackup("/invalid/dir/to/sync/", "/invalid/external/dir/");
        mockService.downloadExternalBackup("/invalid/dir/to/sync/", "/invalid/external/dir/");
    }
}
