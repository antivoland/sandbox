IR = 0 : 0.1 : 1;
LDR = 0 : 100 : 100;

figure(1)
draw(load("rula.strategies"), IR, LDR);
#hold on
#draw(load("rula.strategies.pow"), IR, LDR);
title("RU-LA");

figure(2)
draw(load("laru.strategies"), IR, LDR);
#hold on
#draw(load("laru.strategies.pow"), IR, LDR);
title("LA-RU");

