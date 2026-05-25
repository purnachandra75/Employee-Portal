package com.example.attendance.controller;

import com.example.attendance.model.DocumentSubmission;
import com.example.attendance.model.User;
import com.example.attendance.repository.DocumentSubmissionRepository;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentSubmissionRepository documentSubmissionRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocuments(@RequestParam String username,
                                             @RequestParam("tenth") MultipartFile tenth,
                                             @RequestParam("twelfth") MultipartFile twelfth,
                                             @RequestParam("pc") MultipartFile pc,
                                             @RequestParam("od") MultipartFile od,
                                             @RequestParam("pan") MultipartFile pan,
                                             @RequestParam("aadhaar") MultipartFile aadhaar,
                                             @RequestParam("resume") MultipartFile resume,
                                             @RequestParam("passport") MultipartFile passport) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (tenth.isEmpty() || twelfth.isEmpty() || pc.isEmpty() || od.isEmpty() || pan.isEmpty() || aadhaar.isEmpty() || resume.isEmpty() || passport.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please upload all required documents."));
        }

        DocumentSubmission submission = documentSubmissionRepository.findByUser(user)
                .orElse(new DocumentSubmission(user));

        submission.setTenthFileName(cleanFileName(tenth.getOriginalFilename()));
        submission.setTenthContentType(tenth.getContentType());
        submission.setTenthFileData(tenth.getBytes());

        submission.setTwelfthFileName(cleanFileName(twelfth.getOriginalFilename()));
        submission.setTwelfthContentType(twelfth.getContentType());
        submission.setTwelfthFileData(twelfth.getBytes());

        submission.setPcFileName(cleanFileName(pc.getOriginalFilename()));
        submission.setPcContentType(pc.getContentType());
        submission.setPcFileData(pc.getBytes());

        submission.setOdFileName(cleanFileName(od.getOriginalFilename()));
        submission.setOdContentType(od.getContentType());
        submission.setOdFileData(od.getBytes());

        submission.setPanFileName(cleanFileName(pan.getOriginalFilename()));
        submission.setPanContentType(pan.getContentType());
        submission.setPanFileData(pan.getBytes());

        submission.setAadhaarFileName(cleanFileName(aadhaar.getOriginalFilename()));
        submission.setAadhaarContentType(aadhaar.getContentType());
        submission.setAadhaarFileData(aadhaar.getBytes());

        submission.setResumeFileName(cleanFileName(resume.getOriginalFilename()));
        submission.setResumeContentType(resume.getContentType());
        submission.setResumeFileData(resume.getBytes());

        submission.setPassportFileName(cleanFileName(passport.getOriginalFilename()));
        submission.setPassportContentType(passport.getContentType());
        submission.setPassportFileData(passport.getBytes());

        submission.setSubmittedAt(LocalDateTime.now());

        documentSubmissionRepository.save(submission);

        return ResponseEntity.ok(Map.of("message", "Documents uploaded successfully."));
    }

    @GetMapping("/my-files")
    public ResponseEntity<?> getMyFiles(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<DocumentSubmission> optionalSubmission = documentSubmissionRepository.findByUser(user);
        if (optionalSubmission.isEmpty()) {
            return ResponseEntity.ok(Map.of("exists", false));
        }

        DocumentSubmission submission = optionalSubmission.get();
        Map<String, Object> files = new HashMap<>();
        files.put("tenth", buildDocumentInfo("tenth", "10th Marksheet", submission.getTenthFileName()));
        files.put("twelfth", buildDocumentInfo("twelfth", "12th Marksheet", submission.getTwelfthFileName()));
        files.put("pc", buildDocumentInfo("pc", "PC", submission.getPcFileName()));
        files.put("od", buildDocumentInfo("od", "OD", submission.getOdFileName()));
        files.put("pan", buildDocumentInfo("pan", "PAN Card", submission.getPanFileName()));
        files.put("aadhaar", buildDocumentInfo("aadhaar", "Aadhaar Card", submission.getAadhaarFileName()));
        files.put("resume", buildDocumentInfo("resume", "Resume", submission.getResumeFileName()));
        files.put("passport", buildDocumentInfo("passport", "Passport Photo", submission.getPassportFileName()));

        Map<String, Object> response = new HashMap<>();
        response.put("exists", true);
        response.put("files", files);
        response.put("submittedAt", submission.getSubmittedAt().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/files")
    public ResponseEntity<?> getEmployeeFiles(@PathVariable Long employeeId) {

        Optional<DocumentSubmission> optionalSubmission = documentSubmissionRepository.findByUserId(employeeId);
        List<Map<String, Object>> documents = new ArrayList<>();

        if (optionalSubmission.isEmpty()) {
            documents.add(buildDocumentInfo("tenth", "10th Marksheet", null));
            documents.add(buildDocumentInfo("twelfth", "12th Marksheet", null));
            documents.add(buildDocumentInfo("pc", "PC", null));
            documents.add(buildDocumentInfo("od", "OD", null));
            documents.add(buildDocumentInfo("pan", "PAN Card", null));
            documents.add(buildDocumentInfo("aadhaar", "Aadhaar Card", null));
            documents.add(buildDocumentInfo("resume", "Resume", null));
            documents.add(buildDocumentInfo("passport", "Passport Photo", null));

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> employeeDto = new HashMap<>();
            userRepository.findById(employeeId).ifPresent(user -> {
                employeeDto.put("id", user.getId());
                employeeDto.put("username", user.getUsername());
                employeeDto.put("email", user.getEmail());
                employeeDto.put("fullName", user.getFullName());
            });
            if (employeeDto.isEmpty()) {
                employeeDto.put("id", employeeId);
            }

            response.put("employee", employeeDto);
            response.put("documents", documents);
            response.put("submittedAt", null);
            response.put("status", "Not submitted");
            return ResponseEntity.ok(response);
        }

        DocumentSubmission submission = optionalSubmission.get();
        User user = submission.getUser();

        Map<String, Object> files = new HashMap<>();
        files.put("tenth", buildDocumentInfo("tenth", "10th Marksheet", submission.getTenthFileName()));
        files.put("twelfth", buildDocumentInfo("twelfth", "12th Marksheet", submission.getTwelfthFileName()));
        files.put("pc", buildDocumentInfo("pc", "PC", submission.getPcFileName()));
        files.put("od", buildDocumentInfo("od", "OD", submission.getOdFileName()));
        files.put("pan", buildDocumentInfo("pan", "PAN Card", submission.getPanFileName()));
        files.put("aadhaar", buildDocumentInfo("aadhaar", "Aadhaar Card", submission.getAadhaarFileName()));
        files.put("resume", buildDocumentInfo("resume", "Resume", submission.getResumeFileName()));
        files.put("passport", buildDocumentInfo("passport", "Passport Photo", submission.getPassportFileName()));

        Map<String, Object> employeeDto = new HashMap<>();
        employeeDto.put("id", user.getId());
        employeeDto.put("username", user.getUsername());
        employeeDto.put("email", user.getEmail());
        employeeDto.put("fullName", user.getFullName());

        Map<String, Object> response = new HashMap<>();
        response.put("employee", employeeDto);
        response.put("documents", files.values());
        response.put("submittedAt", submission.getSubmittedAt() != null ? submission.getSubmittedAt().toString() : null);
        response.put("status", "Submitted");
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> buildDocumentInfo(String field, String label, String filename) {
        Map<String, Object> info = new HashMap<>();
        info.put("field", field);
        info.put("label", label);
        info.put("filename", filename);
        return info;
    }

    @GetMapping("/employee/{employeeId}/download/{field}")
    public ResponseEntity<Resource> downloadEmployeeDocument(@PathVariable Long employeeId, @PathVariable String field) {

        DocumentSubmission submission = documentSubmissionRepository.findByUserId(employeeId)
                .orElseThrow(() -> new RuntimeException("No documents found"));

        byte[] fileData = getDataForField(field, submission);
        String fileName = getFileNameForField(field, submission);
        String contentType = getContentTypeForField(field, submission);

        if (fileData == null || fileName == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            if (contentType != null && !contentType.isBlank()) {
                mediaType = MediaType.parseMediaType(contentType);
            }
        } catch (Exception ignored) {
        }

        ByteArrayResource resource = new ByteArrayResource(fileData);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/download/{field}")
    public ResponseEntity<Resource> downloadDocument(@RequestParam String username, @PathVariable String field) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DocumentSubmission submission = documentSubmissionRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No documents found"));

        byte[] fileData = getDataForField(field, submission);
        String fileName = getFileNameForField(field, submission);
        String contentType = getContentTypeForField(field, submission);

        if (fileData == null || fileName == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            if (contentType != null && !contentType.isBlank()) {
                mediaType = MediaType.parseMediaType(contentType);
            }
        } catch (Exception ignored) {
        }

        ByteArrayResource resource = new ByteArrayResource(fileData);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    private byte[] getDataForField(String field, DocumentSubmission submission) {
        return switch (field.toLowerCase()) {
            case "tenth" -> submission.getTenthFileData();
            case "twelfth" -> submission.getTwelfthFileData();
            case "pc" -> submission.getPcFileData();
            case "od" -> submission.getOdFileData();
            case "pan" -> submission.getPanFileData();
            case "aadhaar" -> submission.getAadhaarFileData();
            case "resume" -> submission.getResumeFileData();
            case "passport" -> submission.getPassportFileData();
            default -> null;
        };
    }

    private String getContentTypeForField(String field, DocumentSubmission submission) {
        return switch (field.toLowerCase()) {
            case "tenth" -> submission.getTenthContentType();
            case "twelfth" -> submission.getTwelfthContentType();
            case "pc" -> submission.getPcContentType();
            case "od" -> submission.getOdContentType();
            case "pan" -> submission.getPanContentType();
            case "aadhaar" -> submission.getAadhaarContentType();
            case "resume" -> submission.getResumeContentType();
            case "passport" -> submission.getPassportContentType();
            default -> null;
        };
    }

    private String getFileNameForField(String field, DocumentSubmission submission) {
        return switch (field.toLowerCase()) {
            case "tenth" -> submission.getTenthFileName();
            case "twelfth" -> submission.getTwelfthFileName();
            case "pc" -> submission.getPcFileName();
            case "od" -> submission.getOdFileName();
            case "pan" -> submission.getPanFileName();
            case "aadhaar" -> submission.getAadhaarFileName();
            case "resume" -> submission.getResumeFileName();
            case "passport" -> submission.getPassportFileName();
            default -> null;
        };
    }

    private String cleanFileName(String originalName) {
        return StringUtils.cleanPath(originalName != null ? originalName : "uploaded-file");
    }
}
