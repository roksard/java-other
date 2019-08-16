package ru.roksard.filestorage.dataclass;

import java.util.Date;

public class FileInfo {
	String name;
	String type;
	Date uploadDate;
	String downloadUrl;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((downloadUrl == null) ? 0 : downloadUrl.hashCode());
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
		FileInfo other = (FileInfo) obj;
		if (downloadUrl == null) {
			if (other.downloadUrl != null)
				return false;
		} else if (!downloadUrl.equals(other.downloadUrl))
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



	public String getName() {
		return name;
	}



	public String getType() {
		return type;
	}



	public Date getUploadDate() {
		return uploadDate;
	}



	public String getDownloadUrl() {
		return downloadUrl;
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



	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}



	public FileInfo(String name, String type, Date uploadDate, String downloadUrl) {
		super();
		this.name = name;
		this.type = type;
		this.uploadDate = uploadDate;
		this.downloadUrl = downloadUrl;
	}
}
