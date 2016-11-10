IR = 0 : 0.5 : 1;
LDR = -0.8 : 0.4 : 1.2;

figure(1  )
draw(load("rula.strategies"), IR, LDR);
title("RU-LA");

figure(2)
draw(load("laru.strategies"), IR, LDR);
title("LA-RU");

xlabel("inputRate");
ylabel("lengthDiffRate");
