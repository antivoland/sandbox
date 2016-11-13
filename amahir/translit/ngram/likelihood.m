data = load("laru.known.test");
I = 1 : 1 : rows(data);
IL = data(:, 1);
OL = data(:, 2);
IP = data(:, 3);
OP = data(:, 4);
D = data(:, 5);
L = data(:, 6);

#figure(1)
#plot3(I, IL, L);
#title("LA-RU");
#xlabel("index");
#ylabel("input length");
#zlabel("input probability");

#figure(1)
#qqplot(IL, IP);
#xlabel("input probability");
#ylabel("input length");

#figure(2)
#qqplot(OL, OP);
#xlabel("output probability");
#ylabel("output length");

#figure(3)
#qqplot(IL+OL/2, L, "uniform);
#a = quantile(L,[0, 1]);
#figure(1);
#hist(log10(L)./(IL.+OL)/2);

BP = IP.*OP.*10^100;
ML = (IL.+OL)/2;

figure(1);
hist(L);
#hist(BP.^1/ML);
#hist(1./log(IP));

#figure(2);
#hist(BP.^1/ML);
#hist(1./log(OP));

#LIKELIHOOD = BP

#figure(2);
#hist(L.^(1/log(L)./(IL.+OL)/2));
#xlabel("likelihood");
#ylabel("input/output length");

