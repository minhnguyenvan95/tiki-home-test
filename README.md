Home Test for Software Development Engineer

/src/main/java/Main.java

input `input.csv`

output `output.csv`

- Mô tả thuật toán
    - Lần lượt đọc từng dòng csv và lưu lại theo cấu trúc Map với `key là phoneNumber` và `value là danh sách các Record ( activationDate và deactivationDate )`
    - Khi thêm một Record mới danh sách các Record ứng với phoneNumber sẽ được sắp xếp lại theo giá trị tăng dần của `activationDate`
    - Để tìm một `realActivationDate` ứng với một `phoneNumber` sẽ duyệt ngược mảng Record và kiểm tra điều kiệu ngày `activationDate` của Record mới so sánh với `deactivationDate` của Record cũ xem có lớn hơn 30 ngày không
    - Nếu lớn hơn thì lấy ra được ngày `activationDate` ứng với `realActivationDate` của số điện thoại cần tìm
    

- Các exception handle là:
   - IllegalCsvRecordFormatException ( sai định dạng dòng dữ liệu csv - mỗi dòng dữ liệu phải có ít nhất 2 cột dữ liệu )
   - InvalidRecordDataException ( sai dữ liệu đầu vào , ứng với từng số điện thoại chỉ có thể tồn tại duy nhất Record không chứ ngày `deactivationDate` )