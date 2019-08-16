package ru.roksard.filestorage.service;

import static ru.roksard.filestorage.util.Header.messageHd;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.roksard.filestorage.dataclass.FileInfo;
import ru.roksard.filestorage.dataclass.FileRecord;
import ru.roksard.filestorage.util.MD5;

@Service
public class Storage {
	public enum FilterType {NONE, DOWNLOADED, NOT_DOWNLOADED, ALL}
	
	public ConcurrentHashMap<String, FileRecord> storage = new ConcurrentHashMap<>();
	private boolean busy = false;
	
	public synchronized void lock() {
		while(busy) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		busy = true;
	}
	
	public synchronized void free() {
		busy = false;
		notifyAll();
	}

	@Autowired
	public Storage() {}
	
	public boolean containsFileWithName(String fileName) {
		return getFileByName(fileName) != null;
	}
	
	public FileRecord getFileByName(String fileName) {
		Iterator<FileRecord> it = storage.values().iterator();
		int searchCode = fileName.hashCode();
		while(it.hasNext()) {
			FileRecord file = it.next();
			if(file.getName().hashCode() == searchCode) {
				if(file.getName().equals(fileName))
					return file;
			}
		}
		return null;
	}
	
	public ResponseEntity<String> storeFile(MultipartFile file, boolean force) throws Exception {
		ByteBuffer bytes = ByteBuffer.wrap(file.getBytes()); 
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		String fileHash = MD5.getMd5(bytes.array());
		lock();
		try {
    		if(containsFileWithName(file.getOriginalFilename()) && (!force)) {
    			return ResponseEntity.status(HttpStatus.CONFLICT)
    					.header(messageHd, "File with same name already exists: '"+file.getOriginalFilename()+"'")
    					.header("Access-Control-Expose-Headers", messageHd)
    					.build();
    		}
    		if(storage.containsKey(fileHash) && (!force)) {
    			return ResponseEntity.status(HttpStatus.CONFLICT)
    					.header(messageHd, "File with same contents hash already exists: "
    							+ storage.get(fileHash).getName())
    					.header("Access-Control-Expose-Headers", messageHd)
    					.build();
    		}
		
    		if(force) {
    			FileRecord filerem = getFileByName(file.getOriginalFilename());
        		if(file != null) {
        			storage.remove(filerem.getHash()); 
        		}
    		}
    		storage.put(fileHash, new FileRecord(
				fileHash,
				bytes, 
				file.getOriginalFilename(), 
				file.getContentType(), 
				now));
			return ResponseEntity.created(
					new URI(
							ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.toUriString()
								+ "/getFile?name="
								+file.getOriginalFilename()
					)
			).build();
		} finally {
			free();
		}
	}
	
	public ResponseEntity<String> deleteFile(String name) {
		lock();
		try {
    		FileRecord file = getFileByName(name);
    		if(file != null && (storage.remove(file.getHash()) != null)) 
    			return ResponseEntity.ok()
    					.build();
    		else
    			return ResponseEntity.notFound()
    					.header(messageHd, "file with specified name is not found: '"+name+"'")
    					.header("Access-Control-Expose-Headers", messageHd)
    					.build(); //file with that name was not found or removal didnt happen
		} finally {
			free();
		}
	}
	
	public String[] listFiles(Storage.FilterType filterType) {
		LinkedList<String> fileList = new LinkedList<>();
		switch(filterType) {
		case NONE: break;
		case DOWNLOADED:
			for(FileRecord file : storage.values()) 
				if(file.getDownloadCounter() > 0)
					fileList.add(file.getName());
			
			break;
		case NOT_DOWNLOADED:
			for(FileRecord file : storage.values()) 
				if(file.getDownloadCounter() == 0)
					fileList.add(file.getName());
			break;
		case ALL: 
		default:
			for(FileRecord file : storage.values())
				fileList.add(file.getName());
		}
		return fileList.toArray(new String[fileList.size()]);
	}
	
	public FileInfo[] listFilesDetailed(String nameSearch, int minSize, int maxSize) {
		LinkedList<FileInfo> fileList = new LinkedList<>();
		for(FileRecord file : storage.values()) {
			if(file.getName()
					.toLowerCase()
					.contains(
							nameSearch
							.toLowerCase())) {
				int size = file.getBytes().capacity();
				boolean minOk = true;
				boolean maxOk = true;
				if(minSize != 0 && size < minSize) {
					minOk = false;
				}
				if(maxSize != 0 && size > maxSize) {
					maxOk = false;
				}
				if(minOk && maxOk) { 
					String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString()
							+ "/getFile?name="+file.getName();
					fileList.add(
						new FileInfo(
    						file.getName(),
    						file.getType(),
    						file.getUploadDate(),
    						downloadLink));
				}
			}
				
		}
		return fileList.toArray(new FileInfo[fileList.size()]);
	}

}
