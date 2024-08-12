# Letter To Me - 미래의 나에게 편지를 보내보세요! 
**2024.08.10~**

### 블로그 글
- 기획/디자인
   
<a href=https://learning-study.tistory.com/entry/%ED%86%A0%EC%9D%B4-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%AF%B8%EB%9E%98%EC%9D%98-%EB%82%98%EC%97%90%EA%B2%8C-%ED%8E%B8%EC%A7%80%EB%A5%BC-%EB%B3%B4%EB%82%B4%EB%8A%94-%EC%9B%B9-%EC%84%9C%EB%B9%84%EC%8A%A4-Letter-To-Me-%EA%B8%B0%ED%9A%8D%EB%94%94%EC%9E%90%EC%9D%B8>[개인 프로젝트] 미래의 나에게 편지를 보내는 웹 서비스 Letter To Me - 기획/디자인</a></br>
<a href=https://learning-study.tistory.com/entry/Letter-To-Me-%EB%AF%B8%EB%9E%98%EC%9D%98-%EB%82%98%EC%97%90%EA%B2%8C-%ED%8E%B8%EC%A7%80%EB%A5%BC-%EB%B3%B4%EB%82%B4%EB%8A%94-%EC%9B%B9-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%94%94%EC%9E%90%EC%9D%B8-%EC%88%98%EC%A0%95>[Letter To Me] 미래의 나에게 편지를 보내는 웹 서비스 - 디자인 수정</a></br>

- 백엔드
  
<a href=https://learning-study.tistory.com/entry/Letter-To-Me-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-ERD-%EC%84%A4%EA%B3%84-%EB%B0%B1%EC%97%94%EB%93%9C>[Letter To Me] 프로젝트 ERD 설계 - 백엔드</a></br>
<a href=https://learning-study.tistory.com/entry/Letter-To-Me-API-%EB%AA%85%EC%84%B8%EC%84%9C-%EC%9E%91%EC%84%B1-%EC%99%84%EB%A3%8C-%EB%B0%B1%EC%97%94%EB%93%9C>[Letter To Me] API 명세서 작성 완료 - 백엔드</a></br>

## 목차
- [기획](#기획)
- [디자인](#디자인)
- [백엔드](#백엔드)
  * [ERD설계](#ERD설계)
  * [API명세서](#API명세서)


## 기획
2024.08.10
```
letter To Me (나에게 보내는 편지)

한 줄 주제: 미래의 나에게 보내는 편지

서비스 타겟
- 미래의 나에게 편지를 보내고 싶은 사람
- 미래의 나를 토닥토닥 해주고 싶은 사람
- 시간에 따른 자신의 변화를 확인하고 싶은 사람

서비스 기획 의도
 가끔 미래의 나에게 편지를 쓰고 싶을 때가 있다.
 단순히 미래의 나에게 편지를 보내고 싶을 수도 있고,
 미래의 나에게 힘이 되는 말을 해주고 싶을 때도 있다.

 미래의 내가 과거에 쓴 편지를 보고 어떤 감정을 느낄지도 궁금하고,
 미래의 내가 과거에 나를 보고 힘을 얻을 수도 있을 것 같다.

 혹은 과거의 나의 고민을 읽으며, "별거 아니었네 뭐 ㅋㅋ" 라는 생각을 할 수도 있다.

 재밌을 것 같아서 기획해 봤다.

주요 기능
1. 나에게 편지 작성
 - 나에게 편지를 작성한다. 언제 보낼지는 설정할 수 있다.(1분~N년)
 - 작성중이던 편지를 중간 저장하고 확인 가능

2. 나에게 작성한 편지 확인
 - 설정한 날짜가 되기 전까지는 편지의 내용을 볼 수 없음.
 - 설정한 날짜가 되어 열람이 가능한 / 설정한 날짜가 되지 않아 열람이 불가한 편지
 이렇게 나누면 좋을 것 같다.

3. 이메일에서 편지 확인 가능
 - 자신의 이메일에서 자신이 보낸 편지를 확인 가능.

```

## 디자인
**2024.08.10 ~ 08.11**

<a href=https://learning-study.tistory.com/entry/Letter-To-Me-%EB%AF%B8%EB%9E%98%EC%9D%98-%EB%82%98%EC%97%90%EA%B2%8C-%ED%8E%B8%EC%A7%80%EB%A5%BC-%EB%B3%B4%EB%82%B4%EB%8A%94-%EC%9B%B9-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%94%94%EC%9E%90%EC%9D%B8-%EC%88%98%EC%A0%95>[Letter To Me] 미래의 나에게 편지를 보내는 웹 서비스 - 디자인 수정</a></br>

- 위 블로그 글에 디자인 정리해두었습니다.

## 백엔드
**2024.08.11~**

---

### ERD설계
**2024.08.11**

![image](https://github.com/user-attachments/assets/e9b5a7fa-3e21-48ea-922a-74a0a8e465c9)

<a href=https://learning-study.tistory.com/entry/Letter-To-Me-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-ERD-%EC%84%A4%EA%B3%84-%EB%B0%B1%EC%97%94%EB%93%9C>[Letter To Me] 프로젝트 ERD 설계 - 백엔드</a></br>

---

### API명세서

**2024.08.12**

- API 명세서 노션 링크입니다.

https://fortune-squash-ece.notion.site/Letter-To-Me-API-da31ec7fbd6143e2ade73ec6d4a8d893?pvs=4

<img width="400" alt="image" src="https://github.com/user-attachments/assets/baea1a65-41ff-4521-bc0d-6ddf9d56a420">
<img width="400" alt="image" src="https://github.com/user-attachments/assets/ca7415d4-d22b-495b-8cc0-f2cb4b55e8d7">


<a href=https://learning-study.tistory.com/entry/Letter-To-Me-API-%EB%AA%85%EC%84%B8%EC%84%9C-%EC%9E%91%EC%84%B1-%EC%99%84%EB%A3%8C-%EB%B0%B1%EC%97%94%EB%93%9C>[Letter To Me] API 명세서 작성 완료 - 백엔드</a></br>

- API 명세서 관련 블로그 글 입니다. API 명세서 작성하면서 한 고민들을 작성하였습니다.



---

#  ✏️Commit Message Convention
| Emoticon | Commit Type | Desc |
| --- | --- | --- |
|  ✨  | feat | 새로운 기능 추가 |
| 🐛  | fix | 버그 수정 |
| 📝 | docs | 문서 수정 (md 파일) |
| ♻️  | refactor | 코드 리팩토링 |
| 💄  | style | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
| ✅  | test | 테스트 코드, 리팩토링 테스트 코드 추가 |
| 🚀  | chore | 패키지 매니저 수정 (Dockerfile, gradle, sh, yml) |
| 🚑  | !hotfix | 급하게 치명적인 버그를 고쳐야 하는 경우 |
| 💚  | ci/cd | CI/CD 빌드 시스템 자체를 새로 추가하거나, 그 구조나 설정을 수정하는 커밋 |

