package org.example.file;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FilePractice {

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

}
