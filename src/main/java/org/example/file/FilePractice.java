package org.example.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class FilePractice {

    private final String TEST_FOLDER_PATH = "C:/workspace/test";

    public void test135() throws IOException, InterruptedException {
        //135. 경로감시
        //1) 예시 (감시)폴더 생성
        Path path = Paths.get("C:/workspace/test");
        boolean isExistFolder =  path.toFile().exists();

        if (isExistFolder) {
            System.out.println("폴더 존재함");
        } else {
            System.out.println("폴더 없음, 새폴더 생성함");
            Files.createDirectories(path);
            System.out.println("폴더 생성 완료");
        }

        //2) 폴더 감시 이벤트 등록(생성,수정,삭제)
        WatchService ws = FileSystems.getDefault().newWatchService();

        path.register(ws,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        while(true) {
            WatchKey key = ws.take();
            List<WatchEvent<?>> list = key.pollEvents();
            for (WatchEvent<?> event : list) {
                WatchEvent.Kind<?> kind = event.kind();
                Path eventPath = (Path) event.context();
                if(kind.equals(StandardWatchEventKinds.ENTRY_CREATE)){
                    System.out.println("생성 : " + eventPath.getFileName());
                } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)){
                    System.out.println("삭제 : " + eventPath.getFileName());
                } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)){
                    System.out.println("수정 : " + eventPath.getFileName());
                } else if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                    System.out.println("OVERFLOW");
                }
            }
            if(!key.reset()) break;
        }
        ws.close();

    }

    public void test136() {
        //136. 파일 내용 스트리밍
        //1) Files.lines() 사용
        //try-with-resources사용으로 스트림 닫힐 때 파일도 닫힘.
        System.out.println("1) Files.lines() 사용");

        try (Stream<String> fileStream =
                     Files.lines(Paths.get(TEST_FOLDER_PATH + "/text.txt"), StandardCharsets.UTF_8)) {
            fileStream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //2) BufferedReader.lines() 사용
        System.out.println("2) BufferedReader.lines() 사용");

        try (BufferedReader brStream =
                     Files.newBufferedReader(Paths.get(TEST_FOLDER_PATH + "/text.txt"), StandardCharsets.UTF_8)) {
            brStream.lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 두 방식의 차이점은?
         * BufferedReader: 전통적인 방식, 메모리 사용량이 적고 단순한 파일 처리에 적합.
         * Files.lines(): 함수형 프로그래밍과 스트림 API에 적합하며, 대규모 파일 처리와 병렬 처리에 유리.
         */


    }

}
