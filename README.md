# Mini-TeamProject-IOT_Gateway

미니프로젝트 시작을 앞서 노들 구성을 아래와 작성하였다.
## MQTT 노드 (수정)
<img src="imgs/mqtt.jpeg" height=400 width=900></img>

<각각의 클래스 내용을 소개 작성>

1. **Filter 클래스:**
   - **설명:** 입력 메시지에서 특정 조건을 검사하고 해당 조건을 충족하는 경우 출력 메시지로 전송하는 역할을 하는 노드이다.
   - **필요성:** MQTT 메시지를 필터링하여 원하는 조건을 충족하는 경우에만 메시지를 전송할 수 있도록 한다.

2. **MqttSubscriber 클래스:**
   - **설명:** MQTT 브로커로부터 메시지를 구독하고, 받은 메시지를 OutputNode로 전송하는 클래스이다.
   - **필요성:** 외부 MQTT 브로커로부터 메시지를 구독하여 시스템에 통합할 수 있도록 한다.

3. **ObjectGenerator 클래스:**
   - **설명:** 입력된 메시지에서 특정 필드를 추출하고, 해당 정보를 가지고 새로운 메시지를 생성하는 클래스이다.
   - **필요성:** MQTT 메시지에서 필요한 정보를 추출하여 새로운 형식의 메시지를 생성하여 다양한 용도로 활용할 수 있도록 한다.

4. **NotNullValueChecker 클래스:**
   - **설명:** 주어진 JSON 객체에서 특정 Key의 값이 `null`이 아닌지를 확인하는 Checker 클래스이다.
   - **필요성:** 특정 Key의 값이 `null`이 아닌 경우에만 조건을 충족하도록 필터링할 때 사용됩니다.

5. **SubValueChecker 클래스:**
   - **설명:** 주어진 JSON 객체에서 서브 Key의 리스트를 따라 내려가면서 특정 Key에 대한 값을 확인하는 Checker 클래스이다.
   - **필요성:** JSON 객체의 중첩된 구조에서 특정 Key의 값을 확인하고자 할 때 사용됩니다.

6. **MqttPublisher 클래스:**
   - **설명:** MQTT 클라이언트를 통해 메시지를 브로커에 발행하는 클래스이다.
   - **필요성:** 시스템에서 생성된 메시지를 MQTT 브로커로 발행하여 외부 시스템에서 해당 메시지를 구독할 수 있도록 한다.


## Generation 3
<img src="imgs/gen3.png" height=400 width=900></img>
Generation 3는 우리가 만들었던 기존 XFlow 프로젝트 Node에서 활용하였다.

이로인해 Input, Output 기준이 조금 다를 수 있으니 인지해야한다.

- MQTT In은 Output 기준이며 MQTT Out은 Input으로 가정한다.
- JSONObject를 생성하여 000 한다.
- SensorType Splitter를 사용하여 Type에 따라 Message를 생성한다.
- Null Value Filter에서는 NullCheck를 ooo 한다.


## Generation 4
<img src="imgs/gen4.png" height=400 width=900></img>
노두 수정된 부분 타입에 따라 메세지 생성 설명 
Generation 4에서는 3와 달리 구조가 변경이 된다. 

### 구조변경
- DeviceInfo Key Filter -> Key Filter

- Make Sensor Info -> Sensor Type Filter

### 구조 변경 설명

DeviceInfo Key Filter가 Key Filter로 바뀜으로 Device Key가 아닌 것도 값도 있으니 크게 확장하는 개념으로 변경을 하였다.

Make Sensor Info -> Sensor Type Filter 바뀜으로 


### 필드구성
MQTT Subscriber에서는 필드가 3개가 추가된다.
1. MQTT Client
2. MQTT Connect Options
3. String Topic Filter

set Option() -> main() 

여기서 set Option()는 main()으로 작성한다.


### 특징
여기서 각 클래스의 Filter는 Checker에서 체크 후 필요한 부분만 가져온다. 

또한 Sensor Type은 설정 타입으로 설정을 해준다.



## Generation 8
<img src="imgs/gen8.jpeg" height=400 width=900></img>

### Value Checker
 - AppName Filter
 - NullValue Filter
 - SenesorType Filter

 위 이미지 빨간색 상자 3개는 클래스 공통의 값이라 판단하였고, 아래와 같이 공통으로 값이 들어오게 클래스를 작성하였다. 
 또한 마지막 코드(value) 부분에서는 위 3가지 클래스에 많은 데이터를 받아온다.

## Value Checker.java
![](imgs/valuechecker(value).png)

 


## Generation 9
<img src="imgs/gen9.jpeg" height=400 width=900></img>

### 구조 변경
 Sensor Type Spliter -> Object Generator

### 구조 설명
Commons Topic Generator를 클래스로 두고 Type Spliter를 가지고 둘 다 포용하기 위해 Sensor Type Spliter를 클래스로 만들었다.

### 특징
각 Filter에서 체크는 각각 다르다.

Key Filter는 Key Checker이며, AppName Filter와 Null Value Filter, Sensor Type Filter는 각각 Value Filter에서 각각 체크하여 값을 뽑아낸다. 