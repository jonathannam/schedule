package com.agribank.schedule.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.slugify.Slugify;

@Component
public class FileStoreService {

	@Value("${file.storage.upload.dir}")
	private String UPLOAD_FOLDER;

	@Autowired
	private ResourceLoader resourceLoader;

	public List<String> writeFiles(List<MultipartFile> multipartFiles, String prefix) {
		return multipartFiles.stream().filter(Objects::nonNull).map(file -> {
			try {
				return writeFile(file, prefix);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * @param multipartFile
	 * @param prefix:       user/ui/
	 * @return
	 * @throws IOException
	 */
	public String writeFile(MultipartFile multipartFile, String prefix) throws IOException {
		String dirPath = UPLOAD_FOLDER + prefix;
		File directory = new File(dirPath);
		if (!directory.exists())
			directory.mkdirs();

		String filename = generateFilename(multipartFile.getOriginalFilename());
		Files.write(Paths.get(dirPath + filename), multipartFile.getBytes());
		return prefix + filename;
	}

	public Resource readFile(String filepath) {
		final String FORMAT = String.format("file:%s", UPLOAD_FOLDER + filepath);
		return resourceLoader.getResource(FORMAT);
	}

	@Async
	public void deleteFiles(List<String> filePaths) {
		filePaths.forEach(path -> {
			deleteFile(path);
		});
	}

	@Async
	public void deleteFile(String filePath) {
		File avatarFile = new File(UPLOAD_FOLDER + File.separator + filePath);
		avatarFile.delete();
	}

	public static String generateFilename(String originalFilename) {
		final Slugify slugify = Slugify.builder().locale(Locale.forLanguageTag("vi")).build();

		int index = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(index);
		String name = originalFilename.substring(0, index);

		return slugify.slugify(name).concat("-").concat(String.valueOf(System.currentTimeMillis() / 1000 / 60))
				.concat(ext);
	}
}