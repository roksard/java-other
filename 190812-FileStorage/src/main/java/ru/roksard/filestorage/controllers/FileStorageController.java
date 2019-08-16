package ru.roksard.filestorage.controllers;

import static ru.roksard.filestorage.util.Header.messageHd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;

import ru.roksard.filestorage.dataclass.FileInfo;
import ru.roksard.filestorage.dataclass.FileRecord;
import ru.roksard.filestorage.service.Storage;

@RestController	
@RequestMapping("")
public class FileStorageController {
	
	@Autowired
	Storage storage;
	
	@PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, 
    		@RequestParam(value="force", defaultValue="false") boolean force) {
		try {
			return storage.storeFile(file, force);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
	
	@PostMapping("/updateFile")
    public ResponseEntity<String> updateFile(@RequestParam("file") MultipartFile file) {
		try {
			return storage.storeFile(file, true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
	
	@GetMapping("/getFile")
	public @ResponseBody ResponseEntity<Resource> getFile(@RequestParam("name") String name) {
		try {
			FileRecord file = storage.getFileByName(name);
			if(file != null) {
				Resource resource = new ByteArrayResource(file.getBytes().array());
				file.incDownloadCounter();
				return ResponseEntity.status(HttpStatus.OK)
						.header(HttpHeaders.CONTENT_TYPE, file.getType())
						.body(resource);
			} else 
				return ResponseEntity.notFound()
					.header(messageHd, "file with specified name is not found: '"+name+"'")
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
	
	@PostMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("name") String name) {
		try {
			return storage.deleteFile(name);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
	
	@GetMapping("/listFiles")
    public ResponseEntity<String[]> listFiles(
    		@RequestParam(value="filterMode", defaultValue="1") int filterMode) {
    	try {
    		int type = filterMode % 4; 
    		Storage.FilterType filterType = Storage.FilterType.values()[type];
    		return ResponseEntity.status(HttpStatus.OK)
    				.header(messageHd, "filterMode: 1-only downloaded files, 2-only not downloaded files, "
    						+ "3-all files")
					.header("Access-Control-Expose-Headers", messageHd)
    				.body(storage.listFiles(filterType));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
    
    @GetMapping("/listFilesDetailed")
    public ResponseEntity<FileInfo[]> listFilesDetailed(
    		@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
    		@RequestParam(value="minSize", defaultValue="0") int minSize,
    		@RequestParam(value="maxSize", defaultValue="0") int maxSize) {
    	try {
    		return ResponseEntity.ok(storage.listFilesDetailed(nameSearch, minSize, maxSize));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(messageHd, e.getMessage())
					.header("Access-Control-Expose-Headers", messageHd)
					.build();
		}
    }
}
