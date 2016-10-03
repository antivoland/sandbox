> Для текста требуется определить его небулшитность. Это минимальное количество слов, выкинув которые из текста, мы получим выражение только из нашего словаря. Например:
>  
> ```
> Quick win by Search Engine Optimization Policy
> ```
>  
> Для этой фразы есть два варианта выкидывания слов. Выкинули 3 слова остальные выражения из словаря:
> 
> ```
> Quick win __ ______ ______ Optimization Policy
> ```
>  
> Выкинули 2 слова остальные выражения из словаря:
>  
> ```
> Quick win __ Search Engine Optimization ______
> ```
>  
> Не булшитность – 2.
>  
> Длина булшит фразы до 3 слов. 
>  
> Словарь для вышеприведённого примера:
>  
> ```
> Content Management
> Monoply
> Compelling
> Market Window
> Road map
> Prioritized
> Cross Sell
> Braindump
> Landing Page
> Synergy
> Web 2.0
> Optimization Policy
> Search Engine Optimization
> Communication
> Due dilligence
> Relevance
> Cutting Edge
> Dynamic
> Quick win
> Heads up
> Statement of Work
> Cost Optimization
> Low-Hanging Fruit
> Conversation
> Herding Cats
> ```

Рекурсивное решение содержится в классе [Bullshit](src/main/java/antivoland/amahir/Bullshit.java), ну и один тест, проверяющий корректность решения для описанного случая, можно посмотреть в [BullshitTest](src/test/java/antivoland/amahir/BullshitTest.java).
