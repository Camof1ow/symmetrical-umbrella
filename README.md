<h1 style="text-align: center;"> 당신의 이름은 8.15 </h1>
<p style="text-align: center;">
   <img src="./img1.png" alt="이미지1" style=": center; max-width: 100%; height: auto;" />
</p>

# Table of Contents

- [[1] About the Project](#1-about-the-project)
    - [Features](#features)
    - [Technologies](#technologies)
- [[2] Getting Started](#2-getting-started)
- [[3] Trouble Shooting](#3-trouble-shooting)
- [[4] Contribution](#4-contribution)
- [[5] Acknowledgement](#5-acknowledgement)
- [[6] Contact](#6-contact)
- [[7] License](#7-license)

# [1] About the Project

### 프로젝트의 비전

- [내가춘자라니](https://chunja.vercel.app/) 프로젝트에 감명을 받아 제작하였습니다.
- 광복이라는 주제로 우리가 우리이름이 아닌 일본식 성과 이름을 사용할 수밖에 없었을 비극적 역사에서 <br />광복의 의미를 되새기고 싶었습니다.
- 또한 잊혀진 광복의 독립투사들을 자신의 이름에서 찾아내고 기릴 수 있는 시간이 되었으면 했습니다.

## Features

![video1.webp](video1.webp) ![video2.webp](video2.webp)


### 과도한 요청 방지 
#### 요청 속도 제한
- RateLimitInterceptor: Spring MVC 인터셉터로, 클라이언트 IP 주소를 기반으로 API 요청의 빈도를 제어합니다. 요청이 들어올 때마다 IP 주소를 확인하여, 설정된 요청 한도를 초과한 경우 HTTP 429 상태 코드로 응답하여 과도한 요청을 차단합니다.
- IpBasedRateLimiter: IP 주소별로 요청 수를 제한하는 서비스입니다. Bucket4j 라이브러리를 활용하여 각 IP에 대해 요청 수를 추적하고, 허용된 범위를 초과할 경우 요청을 거부합니다. 이로써 서비스의 남용을 방지하고, 공정한 자원 배분을 유지합니다.
#### 동적 속도 제한 설정
- 피크 시간(9AM-5PM)에 따라 버킷의 용량과 리필 토큰 수를 조정합니다. createNewBucket 및 dynamicBucket 메서드는 요청 제한을 설정하고 관리하기 위해 Bucket4j를 사용하여 생성된 버킷을 반환합니다.

### 이름 변환
- 주어진 성과 이름을 조합하여, 일본식 한자를 한국식으로 변환합니다.
- 성: 데이터베이스에서 성에 해당하는 한자 정보를 조회하거나, 외부 사이트에서 성의 일본어 이름과 발음을 검색합니다.
- 이름: 이름을 분리하여, 성별에 맞는 URL을 통해 외부 사이트에서 일본어 이름과 발음을 검색합니다.
- 정보 결합: 검색된 성과 이름 정보를 조합하여, 최종적으로 이름 정보 응답 객체를 생성합니다.
- 응답 반환: 변환된 성과 이름, 발음, 가구 수 등의 정보를 포함한 응답 객체를 반환합니다.

### 내 이름과 비슷한 독립운동가 찾기
- 전체 텍스트 검색을 통해 결과를 반환합니다. Levenshtein 거리와 FULLTEXT 검색을 사용해 정확도를 높이며, 상위 10개의 결과를 정렬하여 제공합니다.

### 시스템 장애 수리 요청 기능
- 접속한 페이지 정보와 장애를 수집하여 DB에 저장합니다.


## Technologies

### 사용 기술

| **기술**                  | **버전**        | **설명**                                               |
|---------------------------|-----------------|--------------------------------------------------------|
| **Java**                  | 21              | 최신 자바 언어 기능을 사용하여 애플리케이션 구현.      |
| **Spring Boot**           | 3.3.2           | 주요 스타터 포함.                                      |
| **- Spring Boot Starter Web** | -          | 웹 애플리케이션 개발을 위한 핵심 스타터.               |
| **- Spring Boot Starter Data JPA** | -     | JPA를 활용한 데이터베이스 접근 및 관리.               |
| **- Spring Boot Starter Security** | -    | 보안 기능 추가.                                        |
| **Spring Cloud**          | 2023.0.1        | 클라우드 기반 기능 지원.                              |
| **- OpenFeign**           | -               | REST API 클라이언트 구현 도구.                         |
| **- LoadBalancer**        | -               | 클라이언트 측 로드 밸런싱 지원.                       |
| **Lombok**                | 1.18.26         | 반복적인 코드 자동 생성.                              |
| **Resilience4j**          | 2.2.0           | 회복탄력성 기능 제공 (서킷 브레이커 등).               |
| **Jackson**               | 2.13.4 / 2.13.3 | JSON/XML 데이터 처리 및 Java 날짜/시간 API 지원.      |
| **- Jackson Dataformat XML** | 2.13.4        | XML 데이터 처리.                                      |
| **- Jackson Datatype JSR310** | 2.13.3       | Java 8 날짜/시간 API 직렬화 및 역직렬화 지원.         |
| **Jsoup**                 | 1.18.1          | HTML 파싱 및 웹 스크래핑 라이브러리.                   |
| **MySQL Connector/J**     | 8.0.33          | MySQL 데이터베이스와 연결하기 위한 JDBC 드라이버.     |
| **ModelMapper**           | 3.1.1           | 객체 간 매핑을 위한 라이브러리.                        |



# [2] Getting Started

<details>
  <summary>프로젝트 실행하기</summary>

1. **프로젝트 클론**: 이 레포지토리를 클론합니다.
    ```bash
    git clone https://github.com/Camof1ow/symmetrical-umbrella.git
    cd symmetrical-umbrella
    ```

2. **의존성 설치**: Gradle을 사용하여 프로젝트에 필요한 의존성을 설치합니다.
      ```bash
      ./gradlew build
      ```

3. **환경 변수 설정**: `src/main/resources/application.properties` 또는 `src/main/resources/application.yml` 파일을 수정하여 환경 변수를 설정합니다. 예:
    ```properties
    server.port=8080
    spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```

4. **애플리케이션 실행**: 다음 명령어로 애플리케이션을 실행합니다.
    - **IDE에서 실행**: IDE에서 메인 클래스를 실행합니다.
    - **명령줄에서 실행**:
        - **Maven 사용 시**:
          ```bash
          mvn spring-boot:run
          ```
        - **Gradle 사용 시**:
          ```bash
          ./gradlew bootRun
          ```
        - **JAR 파일에서 실행**:
          ```bash
          java -jar target/your-app.jar
          ```

5. **프로젝트 확인**: 브라우저에서 `http://localhost:8080`으로 접속하여 애플리케이션이 정상적으로 실행되는지 확인합니다.

</details>

# [3] Trouble Shooting
### 이름변환기
<details>
  <summary>1. 이름 변환 및 분리 문제</summary>

**발생한 문제:**

입력된 성과 이름이 잘못된 형식으로 주어지면 이름 변환 과정에서 예외가 발생했습니다.

**해결 방법:**

`nameConvert` 메소드에서 `IllegalArgumentException`이 발생한 경우, 입력값이 올바른 형식인지 재검토했습니다. 변환 로직을 수정하여 예외를 보다 명확하게 처리했습니다.

```java
private String nameConvert(String koreanName) {
    try {
        Connection.Response response = Jsoup.connect(NAME_CONVERTER_URL)
                .method(Connection.Method.POST)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .data("contents", koreanName)
                .data("firstinput", "OK")
                .execute();

        Document document = response.parse();
        return document.selectFirst(".finalresult").text();
    } catch (IOException e) {
        e.printStackTrace();
        throw new IllegalArgumentException("Error converting name", e);
    }
}
```

</details>

<details>
  <summary>2. 이름 검색 및 랜덤 선택 문제</summary>

**발생한 문제:**

이름 검색에 사용된 URL이 유효하지 않거나 서버 오류로 인해 `IOException`이 발생했습니다.

**해결 방법:**

`fetchElementsFromUrls` 메소드에서 URL을 정확히 검토하고 서버 접근 문제를 해결하기 위해 오류 카운트 로직을 추가했습니다. URL 리스트에서 모든 요청이 실패할 경우 `IllegalArgumentException`을 발생시켜 문제를 인식하고 처리했습니다. `getRandomElement` 메소드에서 빈 리스트가 반환된 경우 `IllegalStateException`을 처리하여 랜덤 선택 로직의 신뢰성을 높였습니다.

```java
private List<Element> fetchElementsFromUrls(List<String> urls) {
    List<Element> elements = new ArrayList<>();
    AtomicInteger errorCount = new AtomicInteger();

    urls.forEach(
            url -> {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements nameElements = document.select(".name_summary");
                    elements.addAll(nameElements);
                } catch (IOException e) {
                    errorCount.getAndIncrement();
                }
            });

    if (errorCount.get() == urls.size()) {
        throw new IllegalArgumentException("이름을 가져올 수 없어요.");
    }

    return elements;
}

private Element getRandomElement(List<Element> elements) {
    if (elements.isEmpty()) throw new IllegalStateException("No elements found");
    Random random = new Random();
    return elements.get(random.nextInt(elements.size()));
}
```

</details>

<details>
  <summary>3. 빈 결과 처리 문제</summary>

**발생한 문제:**

`fetchElementsFromUrls` 메소드에서 아무런 `Element`도 찾지 못한 경우, 결과가 비어 있어 문제를 발생시켰습니다.

**해결 방법:**

빈 결과가 반환된 경우, URL이 정확한지 확인하고 요청을 재시도하여 문제를 해결했습니다. `fetchElementFromUrl` 메소드에서 `IOException` 발생 시 적절한 예외 처리 및 로그 기록을 통해 문제를 추적했습니다.

```java
private Element fetchElementFromUrl(String url) {
    try {
        Document document = Jsoup.connect(url).get();
        return document.selectFirst(".name_summary");
    } catch (IOException e) {
        throw new RuntimeException("Error fetching data from URL: " + url, e);
    }
}
```

</details>

<details>
  <summary>4. 데이터 파싱 문제</summary>

**발생한 문제:**

`parseHouseholds` 메소드에서 가계수(`households`)를 파싱할 때 `Element`가 null일 경우 기본값으로 `9999`를 사용했습니다.

**해결 방법:**

`parseHouseholds` 메소드에서 `Element`가 null일 때 기본값을 설정하도록 로직을 추가했습니다. 가계수 텍스트에서 숫자만 추출하여 `Integer.parseInt`로 파싱하는 부분을 검토하고, 예외 처리를 강화했습니다.

```java
private int parseHouseholds(Element element) {
    if (element == null) return 9999;
    String householdsText = element.text();
    int i = element.text().lastIndexOf(':');
    String substring = householdsText.substring(i + 1);
    String numbersOnly = substring.replaceAll("[^0-9]", "");
    return Integer.parseInt(numbersOnly);
}
```

</details>

### 독립운동가 찾기
<details>
  <summary>1. API 데이터 가져오기 오류</summary>

**문제 발생:** API 데이터를 가져오는 과정에서 `activistClient.getApiResponse` 호출 결과가 `null`이거나, 응답 데이터 형식이 예상과 달라 XML 파싱 과정에서 예외가 발생하였다.

**해결 방법:** 문제를 해결하기 위해, `activistClient.getApiResponse` 메소드 호출 시 발생할 수 있는 예외를 처리하기 위해 try-catch 블록을 추가하였다. 또한, 응답 데이터가 `null`인 경우와 XML 파싱 중 오류가 발생할 경우를 대비하여 적절한 로그를 남기고 `null` 값을 반환하도록 하였다.

```java
private FamilyKeysAndPageCount getFamilyKeyAndPageCount(String key) {
    String response = activistClient.getApiResponse("4", key, 1);
    if (Objects.isNull(response)) {
        log.error("Failed to get. key: '{}'", key);
        return null;
    }
    ActivistOpenApiResponse activistOpenApiResponse = parseXmlResponse(response);
    if (Objects.isNull(activistOpenApiResponse)) {
        log.error("Failed to parse. key: '{}'", key);
        return null;
    }
    return new FamilyKeysAndPageCount(key, activistOpenApiResponse.getPageCount());
}
```

```java
private ActivistOpenApiResponse fetchPage(String key, int page) {
    String response = activistClient.getApiResponse("4", key, page);
    if (Objects.isNull(response)) {
        log.error("Failed to get. key: '{}', page: '{}'", key, page);
        return null;
    }
    ActivistOpenApiResponse activistOpenApiResponse = parseXmlResponse(response);
    if (Objects.isNull(activistOpenApiResponse)) {
        log.error("Failed to parse. key: '{}', page: '{}'", key, page);
        return null;
    }
    return activistOpenApiResponse;
}
```

</details>

<details>
  <summary>2. XML 파싱 오류</summary>

**문제 발생:** XML 파싱을 시도하는 과정에서 XML 문자열이 잘못된 형식이거나, `XmlMapper` 사용 중 예외가 발생하여 XML 파싱이 실패하였다.

**해결 방법:** `parseXmlResponse` 메소드에서 XML 파싱 중 예외가 발생할 수 있는 상황을 처리하기 위해 try-catch 블록을 사용하였다. 또한, XML 문자열이 유효하지 않은 경우를 대비하여 디버그 로그를 추가하여 문제를 쉽게 추적할 수 있도록 했다.

```java
private ActivistOpenApiResponse parseXmlResponse(String xmlString) {
    try {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ActivistOpenApiResponse response = xmlMapper.readValue(xmlString, ActivistOpenApiResponse.class);
        log.debug("Parsed response: {}", response);
        return response;
    } catch (Exception e) {
        log.error("Error parsing XML: {}", e.getMessage(), e);
        log.debug("error XML: {}", xmlString);
        return null;
    }
}
```

</details>

<details>
  <summary>3. 이름 검색 오류</summary>

**문제 발생:** 이름 검색을 수행하는 과정에서 쿼리 실행 중 오류가 발생하거나 검색 결과가 비어 있는 경우가 발생하였다.

**해결 방법:** 검색 메소드에서 발생할 수 있는 예외를 처리하기 위해 try-catch 블록을 사용하였다. 또한, 검색 결과가 비어 있을 경우를 대비하여 적절한 로그를 남기고, 비어 있는 결과를 처리할 수 있도록 했다. 이로 인해 쿼리 오류와 비어 있는 결과에 대한 문제를 효과적으로 해결할 수 있었다.

```java
@Transactional
public List<ActivistResponse> findSameOrSimilarName(String name) {
    List<Activist> searchResults;
    searchResults = activistRepository.findBySimilarName(name);

    if(searchResults == null || searchResults.isEmpty()){
        searchResults = activistRepository.findByFullTextSearch(name);
    }
    if(searchResults == null || searchResults.isEmpty()){
        searchResults = activistRepository.findTop10ByNameContaining(name);
    }

    if (searchResults.isEmpty()) {
        log.error("No activists found with name: {}", name);
        return Collections.emptyList();
    }

    if(searchResults.size() > 10){
        searchResults = searchResults.subList(0, 9);
    }

    log.info("[search] search name : '{}', response names: {}",
        name, searchResults.stream().map(Activist::getName).toList());

    return proceedActivists(searchResults);
}
```

</details>

<details>
  <summary>4. 이미지 다운로드 및 저장 오류</summary>

**문제 발생:** 이미지 다운로드 및 저장 과정에서 이미지 URL 오류나 네트워크 문제로 인해 이미지 다운로드 실패 또는 파일 시스템 오류가 발생하였다.

**해결 방법:** 이미지 다운로드 중 네트워크 오류와 파일 시스템 오류를 처리하기 위해 try-catch 블록을 추가하였다. 다운로드 중 발생할 수 있는 오류를 로그로 기록하고, 이미지 저장 실패를 처리할 수 있도록 했다.

```java
private String downloadImage(String imageUrl) {
    String fileName = UUID.randomUUID() + ".jpg";
    Path path = Paths.get(IMAGE_DIRECTORY + fileName);

    try {
        String targetUrl = "https://search.i815.or.kr" + imageUrl;
        URL url = new URL(targetUrl);

        try (InputStream in = url.openStream();
            ReadableByteChannel rbc = Channels.newChannel(in);
            FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        return fileName;
    } catch (IOException e) {
        log.error("Error downloading image: {}", e.getMessage());
        return null;
    }
}
```

```java
private void updateDatabase(String chineseName, String imagePath) {
    if (imagePath != null) {
        activistRepository.findByNameHanja(chineseName)
            .ifPresent(activist -> {
                //유니크한 값이 아니라서 여러명이 나올 수 있음. 누군지 모르지 일단 업데이트 하지 않음
                if(activist.size() > 1){
                    return;
                }
                Activist first = activist.getFirst();
                first.setImagePath(imagePath);
                activistRepository.save(first);
            });
    }
}
```

</details>

### 요청 속도 제한

<details>
  <summary>1. IP 기반 Rate Limiting 오류</summary>

**문제 발생:** `IpBasedRateLimiter`에서 `ConcurrentHashMap`을 사용하여 IP 별로 Bucket을 관리하는 과정에서 Bucket이 제대로 생성되지 않거나, Token이 올바르게 소비되지 않는 문제가 발생하였다. 이로 인해 올바르지 않은 Rate Limiting 동작을 유발할 수 있었다.

**해결 방법:** Bucket 생성 및 Token 소비를 확인하기 위해 `resolveBucket`과 `tryConsume` 메소드에 적절한 로그를 추가하여 문제를 추적하였다. `ConcurrentHashMap.computeIfAbsent` 메소드 사용으로 Bucket을 안전하게 생성하고, `Bucket.tryConsumeAndReturnRemaining` 메소드로 Token 소비 상태를 체크하였다.

```java
public Bucket resolveBucket(String ip) {
    return buckets.computeIfAbsent(ip, k -> {
        log.debug("Creating new bucket for IP: {}", ip);
        return throttlingConfig.createNewBucket();
    });
}

public boolean tryConsume(String ip) {
    Bucket bucket = resolveBucket(ip);
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    if (probe.isConsumed()) {
        log.debug("Request allowed for IP: {}. Remaining tokens: {}", ip, probe.getRemainingTokens());
        return true;
    } else {
        log.debug("Request denied for IP: {}. Need to wait {} seconds", ip, probe.getNanosToWaitForRefill() / 1_000_000_000);
        return false;
    }
}
```

</details>

<details>
  <summary>2. Rate Limit Interceptor 오류</summary>

**문제 발생:** `RateLimitInterceptor`에서 클라이언트 IP를 추출하여 Rate Limiting을 적용하는 과정에서 IP 추출 로직이 올바르게 작동하지 않거나, `IpBasedRateLimiter`와의 연동 문제가 발생하였다. 이로 인해 Rate Limiting이 제대로 적용되지 않거나, API 요청이 차단되지 않는 문제가 발생하였다.

**해결 방법:** IP 추출 로직과 Rate Limiting 처리를 검토하여 클라이언트 IP를 정확히 추출하고, `IpBasedRateLimiter`의 `tryConsume` 메소드 호출 결과에 따라 적절히 응답을 설정하도록 했다. IP 추출 로직에 대한 로깅을 추가하여 문제를 쉽게 파악할 수 있도록 했다.

```java
@Override
public boolean preHandle(HttpServletRequest request,
    HttpServletResponse response, Object handler) throws Exception {

    String ip = getClientIp(request);

    if (rateLimiter.tryConsume(ip)) {
        return true;
    }

    response.sendError(
        HttpStatus.TOO_MANY_REQUESTS.value(),
        "You have exhausted your API Request Quota"
    );
    return false;
}

private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
    return ip;
}
```

</details>

<details>
  <summary>3. ThrottlingConfig의 Rate Limiting 설정 오류</summary>

**문제 발생:** `ThrottlingConfig`에서 Rate Limiting 설정이 잘못되어 피크 시간과 비피크 시간에 대한 Bucket의 용량과 리필 토큰 수가 잘못 설정되는 문제가 발생하였다. 이로 인해 Rate Limiting 동작이 비정상적으로 작동할 수 있었다.

**해결 방법:** Rate Limiting 설정 메소드에서 피크 시간과 비피크 시간에 따른 용량과 리필 토큰 수를 올바르게 설정하도록 검토하였다. `isPeakHour` 메소드를 사용하여 피크 시간 여부를 확인하고, 이를 기반으로 적절한 Rate Limiting 설정을 적용하였다.

```java
@Bean
public Bucket dynamicBucket() {
    return Bucket.builder()
        .addLimit(
            Bandwidth.classic(getCapacity(),
            Refill.intervally(getRefillTokens(), Duration.ofMinutes(1)))
        ).build();
}

private long getCapacity() {
    return isPeakHour() ? 20 : 30;
}

private long getRefillTokens() {
    return isPeakHour() ? 20 : 30;
}

public boolean isPeakHour() {
    int hour = LocalTime.now().getHour();
    return hour >= 9 && hour < 17;  // 9AM to 5PM을 피크 시간으로 가정
}
```

</details>

# [4] Contribution

- ✨[Camof1ow](https://github.com/Camof1ow) : 좋아요, 기능수리 요청 및 [내이름은?](https://yourname815.vercel.app/myname) 기능개발.
- ✨[dlo](https://github.com/Kang-YongHo) : 서버 보안 기능 및 [독립운동가 찾기](https://yourname815.vercel.app/activist) 기능 개발.

# [5] Acknowledgement

- [내가 춘자라니](https://chunja.vercel.app/)
- AWS
- [FRONTEND Repository](https://github.com/Camof1ow/your-name-815)

# [6] Contact

- 📧 g_10000@kakao.com
- 📋 [contact](https://camof1ow.github.io/#three)

# [7] License

[![License: CC BY-ND 4.0](https://licensebuttons.net/l/by-nd/4.0/80x15.png)](https://creativecommons.org/licenses/by-nd/4.0/)

