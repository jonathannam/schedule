import moment from "moment";
import "moment/locale/vi"; // Import locale tiếng Việt cho moment
import React, { useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import "react-big-calendar/lib/css/react-big-calendar.css";
import events from "./events";

// Cài đặt ngôn ngữ và cấu hình locale cho Việt Nam
moment.locale("vi");
const localizer = momentLocalizer(moment);

export default function ReactBigCalendar() {
  const [eventsData, setEventsData] = useState(events);

  const handleSelect = ({ start, end }) => {
    const title = window.prompt("Hãy chọn lịch");
    if (title) {
      setEventsData([
        ...eventsData,
        {
          start,
          end,
          title,
        },
      ]);
    }
  };

  const handleEdit = (event) => {
    const title = window.prompt("Hãy sửa lại lịch");
    const start = event.start
    const end = event.end
    if (title) {
      setEventsData([
        ...eventsData,
        {
          start,
          end,
          title,
        },
      ]);
    }
  };
  const dayPropGetter = (date) => {
    const style = {};
    if (date.getDay() === 0 || date.getDay() === 6) {
      style.backgroundColor = "#f5f5f5"; // Tô màu nền cho ngày cuối tuần
    }
    return {
      style,
    };
  };

  return (
    <div className="App">
      <Calendar
        views={["day", "agenda", "week", "month"]}
        selectable
        localizer={localizer}
        defaultDate={new Date()}
        defaultView="month"
        events={eventsData}
        style={{ height: "100vh" }}
        onSelectEvent={(event) => handleEdit(event)}
        onSelectSlot={handleSelect}
        culture="vi"
        dayPropGetter={dayPropGetter}
        messages={{
          allDay: 'Cả ngày',
          previous: 'Trước',
          next: 'Tiếp',
          today: 'Hôm nay',
          month: 'Tháng',
          week: 'Tuần',
          day: 'Ngày',
          agenda: 'Lịch trình',
          date: 'Ngày',
          time: 'Thời gian',
          event: 'Sự kiện',
          noEventsInRange: 'Không có sự kiện nào trong khoảng thời gian này.',
          showMore: (total) => `+ Xem thêm (${total})`,
        }}
      />
    </div>
  );
}