package ru.roksard.filestorage.dataclass;

import java.nio.ByteBuffer;
import java.util.Date;


public class FileRecord {
	
	String hash;
	ByteBuffer bytes;
	String name;
	String type;
	Date uploadDate;
	int downloadCounter = 0;
	
	public int getDownloadCounter() {
		return downloadCounter;
	}

	public void setDownloadCounter(int downloadCounter) {
		this.downloadCounter = downloadCounter;
	}
	
	public void incDownloadCounter() {
		downloadCounter++;
	}

	public FileRecord() {}
	
	public FileRecord(String hash, ByteBuffer bytes, String name, String type, Date uploadDate) {
		super();
		this.hash = hash;
		this.bytes = bytes;
		this.name = name;
		this.type = type;
		this.uploadDate = uploadDate;
	}
	

	public String getHash() {
		return hash;
	}

	public ByteBuffer getBytes() {
		return bytes;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setBytes(ByteBuffer bytes) {
		this.bytes = bytes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	@Override
	public String toString() {
		return "FileRecord [name=" + name + ", type=" + type + ", uploadDate=" + uploadDate + 
				", bytes(size="+bytes.array().length +")]";
	}

	public boolean equalsFileHash(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileRecord other = (FileRecord) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}

	public boolean equalsFileName(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileRecord other = (FileRecord) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bytes == null) ? 0 : bytes.hashCode());
		result = prime * result + downloadCounter;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((uploadDate == null) ? 0 : uploadDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileRecord other = (FileRecord) obj;
		if (bytes == null) {
			if (other.bytes != null)
				return false;
		} else if (!bytes.equals(other.bytes))
			return false;
		if (downloadCounter != other.downloadCounter)
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (uploadDate == null) {
			if (other.uploadDate != null)
				return false;
		} else if (!uploadDate.equals(other.uploadDate))
			return false;
		return true;
	}

}
