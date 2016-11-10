function draw(data, IR, LDR)
  STD = reshape(data(:,3), columns(LDR), columns(IR));
  mesh(IR, LDR, STD);
endfunction
