package io.redispro.redisexec.listener;

import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.redispro.redisexec.dto.Book;
import org.bson.Document;


@Component
public class MongoEventListener {

    //@Autowired
    private final MongoTemplate mongoTemplate;

    /**
     * 생성자 주입 방식은 @Autowired 어노테이션을 생성자에 붙여 의존성을 주입하는 방식입니다. 스프링 4.3 이상에서는 @Autowired를 생성자에 명시하지 않아도 스프링이 자동으로 주입해줍니다. 이 방법이 가장 권장되는 방식입니다.
     */
    // 생성자 주입 방식
    @Autowired // 스프링 4.3 이상에서는 @Autowired 생략 가능
    public MongoEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * MongoDB는 JPA와 같은 관계형 데이터베이스의 ORM(Object-Relational Mapping)과는 다른 방식으로 동작하기 때문에, JPA에서 제공하는 @PostPersist, @PrePersist, @PostLoad, @PreRemove 등의 라이프사이클 콜백 어노테이션을 지원하지 않습니다. 그 이유는 MongoDB의 데이터 모델과 JPA의 관계형 데이터 모델이 본질적으로 다르기 때문입니다.
     * <p>
     * 1. 관계형 데이터베이스 vs NoSQL 데이터베이스
     * **JPA (Java Persistence API)**는 관계형 데이터베이스의 객체와 테이블 간의 매핑을 관리하는 표준입니다. 데이터베이스의 라이프사이클과 트랜잭션 관리를 잘 지원하기 위해 @PostPersist, @PrePersist 등과 같은 콜백을 제공합니다.
     * MongoDB는 NoSQL 데이터베이스로, 문서 기반 저장소(Document Store)입니다. MongoDB는 객체-관계 매핑(ORM)이 아니라 객체-문서 매핑(ODM)을 사용합니다. MongoDB는 데이터를 테이블의 행처럼 저장하지 않기 때문에, JPA와 같은 방식으로 엔티티의 라이프사이클을 추적할 필요가 없습니다.
     * 2. MongoDB의 데이터 처리 방식
     * MongoDB에서는 데이터를 문서 형식으로 저장하며, 각 문서는 독립적으로 존재합니다. MongoDB는 데이터 저장 및 조회가 매우 빠르며, 스키마가 유연합니다. 이런 특성 때문에 MongoDB는 JPA에서 제공하는 "삽입, 수정, 삭제 후"와 같은 이벤트를 필요로 하지 않습니다.
     * <p>
     * 3. 라이프사이클 관리 차이
     * JPA: @PostPersist, @PrePersist, @PostLoad 등은 데이터베이스의 상태 변화와 트랜잭션을 고려하여 엔티티 객체의 상태를 변경하기 위한 콜백입니다. 이러한 어노테이션은 주로 관계형 데이터베이스에서 트랜잭션이 관리되는 방식과 밀접하게 관련이 있습니다.
     * MongoDB: MongoDB는 트랜잭션 기반의 관계형 데이터베이스와 달리, 일반적으로 트랜잭션을 사용하지 않거나 필요한 범위가 다릅니다. MongoDB에서 문서가 저장될 때 트랜잭션을 사용하는 방식과 라이프사이클 이벤트를 결합하는 것은 JPA와는 다른 방식으로 동작해야 하므로, MongoDB에서는 이러한 @PostPersist, @PrePersist 등의 기능이 지원되지 않습니다.
     * 4. MongoDB에서의 후처리 방법
     * MongoDB에서는 @PostPersist, @PrePersist와 같은 콜백을 대신하여 다음과 같은 방법으로 후처리를 할 수 있습니다:
     * <p>
     * '@EventListener (AfterLoadEvent): MongoDB에서 문서를 로드한 후 후처리 작업을 하기 위해 @EventListener와 AfterLoadEvent를 사용할 수 있습니다. 이 방식은 MongoDB의 데이터 로드 후 후속 처리를 트리거할 수 있습니다.
     * MongoDB 라이프사이클 후킹: Spring Data MongoDB에서는 @AfterLoad와 같은 이벤트를 사용하여 데이터를 로드한 후 객체에 대해 후처리를 할 수 있습니다.
     * MongoTemplate 사용: MongoDB에서 MongoTemplate을 사용하여 객체를 직접 로드한 후 후처리할 수 있습니다.
     * 결론
     * MongoDB는 JPA와는 다르게 관계형 데이터베이스에서 제공하는 @PostPersist, @PrePersist, @PostLoad 같은 어노테이션을 지원하지 않습니다. 대신 MongoDB에서는 다른 방식으로 객체의 라이프사이클과 후처리를 관리하며, Spring Data MongoDB에서 제공하는 이벤트 리스너를 활용하거나, MongoTemplate을 사용하여 데이터를 조회한 후 후속 작업을 처리할 수 있습니다. MongoDB의 설계 철학과 데이터 모델에 맞는 방식으로 데이터를 처리하기 위해 이런 접근 방식이 필요합니다.
     */
    @EventListener
    public void handleAfterLoadEvent(AfterLoadEvent<?> event) {

        /*
        아래는 예제 코드 일분 실제 동작을 위한 코드가 아닙니다.
         */
        // getSource()가 Document 타입이므로 직접 처리
        Document document = event.getSource();  // 직접 캐스팅

        // Document에서 Book 객체로 변환
        // 변환된 book 객체 사용
        // 필요에 따라 처리
        Book book = mongoTemplate.getConverter().read(Book.class, document);
    }
}
